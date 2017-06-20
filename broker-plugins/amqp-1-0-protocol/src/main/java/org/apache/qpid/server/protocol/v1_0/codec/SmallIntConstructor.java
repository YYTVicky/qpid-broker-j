/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.qpid.server.protocol.v1_0.codec;

import java.util.List;

import org.apache.qpid.server.bytebuffer.QpidByteBufferUtils;
import org.apache.qpid.server.protocol.v1_0.type.AmqpErrorException;
import org.apache.qpid.server.protocol.v1_0.type.transport.ConnectionError;
import org.apache.qpid.server.protocol.v1_0.type.transport.Error;
import org.apache.qpid.server.bytebuffer.QpidByteBuffer;

public class SmallIntConstructor implements TypeConstructor<Integer>
{
    private static final SmallIntConstructor INSTANCE = new SmallIntConstructor();


    public static SmallIntConstructor getInstance()
    {
        return INSTANCE;
    }

    private SmallIntConstructor()
    {
    }

    @Override
    public Integer construct(final List<QpidByteBuffer> in, final ValueHandler handler) throws AmqpErrorException
    {
        if(QpidByteBufferUtils.hasRemaining(in))
        {
            byte b = QpidByteBufferUtils.get(in);
            return (int) b;
        }
        else
        {
            Error error = new Error();
            error.setCondition(ConnectionError.FRAMING_ERROR);
            error.setDescription("Cannot construct int: insufficient input data");
            throw new AmqpErrorException(error);
        }
    }
}
