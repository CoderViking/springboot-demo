package com.viking.elasticsearch.elasticsearch.transportclient;

import com.viking.elasticsearch.config.TransportClientHelper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created By Viking on 2020/3/9
 */
//@Component
public class EsIndexManageUtil {
    public static enum Result implements Writeable {
        CREATED(0),
        UPDATED(1),
        DELETED(2),
        NOT_FOUND(3),
        NOOP(4);

        private final byte op;
        private final String lowercase;

        private Result(int op) {
            this.op = (byte)op;
            this.lowercase = this.toString().toLowerCase(Locale.ENGLISH);
        }

        public byte getOp() {
            return this.op;
        }

        public String getLowercase() {
            return this.lowercase;
        }

        public static Result readFrom(StreamInput in) throws IOException {
            Byte opcode = in.readByte();
            switch(opcode) {
                case 0:
                    return CREATED;
                case 1:
                    return UPDATED;
                case 2:
                    return DELETED;
                case 3:
                    return NOT_FOUND;
                case 4:
                    return NOOP;
                default:
                    throw new IllegalArgumentException("Unknown result code: " + opcode);
            }
        }

        public void writeTo(StreamOutput out) throws IOException {
            out.writeByte(this.op);
        }
    }
}
