/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.qpid.tests.protocol.v0_8;

import java.net.InetSocketAddress;

import org.apache.qpid.server.model.Protocol;
import org.apache.qpid.server.plugin.ProtocolEngineCreator;
import org.apache.qpid.server.plugin.QpidServiceLoader;
import org.apache.qpid.server.protocol.ProtocolVersion;
import org.apache.qpid.tests.protocol.AbstractFrameTransport;


public class FrameTransport extends AbstractFrameTransport<Interaction>
{
    private final byte[] _protocolHeader;
    private ProtocolVersion _protocolVersion;

    FrameTransport(final InetSocketAddress brokerAddress)
    {
        this(brokerAddress, Protocol.AMQP_0_9_1);
    }

    FrameTransport(final InetSocketAddress brokerAddress, Protocol protocol)
    {
        super(brokerAddress, new FrameDecoder(getProtocolVersion(protocol)), new FrameEncoder());
        _protocolVersion = getProtocolVersion(protocol);
        byte[] protocolHeader = null;
        for(ProtocolEngineCreator installedEngine : (new QpidServiceLoader()).instancesOf(ProtocolEngineCreator.class))
        {
            if (installedEngine.getVersion() == protocol)
            {
                protocolHeader = installedEngine.getHeaderIdentifier();
            }
        }

        if (protocolHeader == null)
        {
            throw new IllegalArgumentException(String.format("Unsupported protocol %s", protocol));
        }
        _protocolHeader = protocolHeader;
    }

    @Override
    public FrameTransport connect()
    {
        super.connect();
        return this;
    }

    public Interaction newInteraction()
    {
        return new Interaction(this);
    }

    public byte[] getProtocolHeader()
    {
        return _protocolHeader;
    }

    ProtocolVersion getProtocolVersion()
    {
        return _protocolVersion;
    }

    private static ProtocolVersion getProtocolVersion(Protocol protocol)
    {
        final ProtocolVersion protocolVersion;
        switch (protocol)
        {
            case AMQP_0_8:
                protocolVersion = ProtocolVersion.v0_8;
                break;
            case AMQP_0_9_1:
                protocolVersion = ProtocolVersion.v0_91;
                break;
            case AMQP_0_9:
                protocolVersion = ProtocolVersion.v0_9;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported protocol %s", protocol));
        }
        return protocolVersion;
    }
}
