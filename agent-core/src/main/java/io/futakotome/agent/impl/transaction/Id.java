package io.futakotome.agent.impl.transaction;

import com.dslplatform.json.JsonWriter;
import io.futakotome.agent.objectpool.Recyclable;
import io.futakotome.agent.utils.HexUtils;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Id implements Recyclable {
    private final byte[] data;
    private boolean empty = true;

    @Nullable
    private String cachedStringRepresentation;

    public static Id new128BitId() {
        return new Id(16);
    }

    public static Id new64BitId() {
        return new Id(8);
    }

    private Id(int idLengthBytes) {
        data = new byte[idLengthBytes];
    }

    public void setToRandomValue() {
        setToRandomValue(ThreadLocalRandom.current());
    }

    public void setToRandomValue(Random randomValue) {
        randomValue.nextBytes(data);
        onMutation(false);
    }

    public void fromHexString(String hexEncodedString, int offset) {
        HexUtils.nextBytes(hexEncodedString, offset, data);
        onMutation();
    }

    public int fromBytes(byte[] bytes, int offset) {
        System.arraycopy(bytes, offset, data, 0, data.length);
        onMutation();
        return data.length;
    }

    public int toBytes(byte[] bytes, int offset) {
        System.arraycopy(data, 0, bytes, offset, data.length);
        return offset + data.length;
    }

    public void fromLongs(long... values) {
        if (values.length * Long.BYTES != data.length) {
            throw new IllegalArgumentException("Invalid number of long values");
        }
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        for (long value : values) {
            buffer.putLong(value);
        }
        onMutation();
    }

    @Override
    public void resetState() {
        Arrays.fill(data, (byte) 0);
        onMutation(true);
    }

    public void copyFrom(Id other) {
        System.arraycopy(other.data, 0, data, 0, data.length);
        this.cachedStringRepresentation = other.cachedStringRepresentation;
        this.empty = other.empty;
    }

    private void onMutation() {
        onMutation(isAllZeros(data));
    }

    private void onMutation(boolean empty) {
        cachedStringRepresentation = null;
        this.empty = empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id that = (Id) o;
        return Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public String toString() {
        String s = cachedStringRepresentation;
        if (s == null) {
            s = cachedStringRepresentation = HexUtils.bytesToHex(data);
        }
        return s;
    }

    public boolean dataEquals(byte[] data, int offset) {
        byte[] thisData = this.data;
        for (int i = 0; i < thisData.length; i++) {
            if (thisData[i] != data[i + offset]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAllZeros(byte[] bytes) {
        for (byte b : bytes) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public void writeAsHex(JsonWriter jw) {
        HexUtils.writeBytesAsHex(data, jw);
    }

    public void writeAsHex(StringBuilder sb) {
        HexUtils.writeBytesAsHex(data, sb);
    }

    public long getLeastSignificantBits() {
        return readLong(data.length - 8);
    }

    public long readLong(int offset) {
        long lsb = 0;
        for (int i = offset; i < offset + 8; i++) {
            lsb = (lsb << 8) | (data[i] & 0xff);
        }
        return lsb;
    }

    int getLength() {
        return data.length;
    }
}
