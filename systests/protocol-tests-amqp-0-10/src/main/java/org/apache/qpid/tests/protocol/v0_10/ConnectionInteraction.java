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
package org.apache.qpid.tests.protocol.v0_10;

import org.apache.qpid.server.protocol.v0_10.transport.ConnectionOpen;
import org.apache.qpid.server.protocol.v0_10.transport.ConnectionStartOk;
import org.apache.qpid.server.protocol.v0_10.transport.ConnectionTuneOk;

public class ConnectionInteraction
{
    public static final String SASL_MECHANISM_ANONYMOUS = "ANONYMOUS";
    public static final String SASL_MECHANISM_PLAIN = "PLAIN";

    private final Interaction _interaction;
    private ConnectionStartOk _startOk;
    private ConnectionTuneOk _tuneOk;
    private ConnectionOpen _open;

    public ConnectionInteraction(final Interaction interaction)
    {
        _interaction = interaction;
        _startOk = new ConnectionStartOk();
        _tuneOk = new ConnectionTuneOk();
        _open = new ConnectionOpen();
    }

    public Interaction startOk() throws Exception
    {
        return _interaction.sendPerformative(_startOk);
    }

    public ConnectionInteraction startOkMechanism(final String mechanism)
    {
        _startOk.setMechanism(mechanism);
        return this;
    }

    public Interaction tuneOk() throws Exception
    {
        return _interaction.sendPerformative(_tuneOk);
    }

    public Interaction open() throws Exception
    {
        return _interaction.sendPerformative(_open);
    }

    public ConnectionInteraction tuneOkChannelMax(final int channelMax)
    {
        _tuneOk.setChannelMax(channelMax);
        return this;
    }

    public ConnectionInteraction tuneOkMaxFrameSize(final int maxFrameSize)
    {
        _tuneOk.setMaxFrameSize(maxFrameSize);
        return this;
    }

    public ConnectionInteraction startOkResponse(final byte[] response)
    {
        _startOk.setResponse(response);
        return this;
    }
}
