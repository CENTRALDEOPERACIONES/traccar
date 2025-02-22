/*
 * Copyright 2013 - 2019 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.NetworkMessage;
import org.traccar.Protocol;
import org.traccar.helper.BitUtil;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CanVariable;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.Date;

public class CellocatorProtocolDecoder extends BaseProtocolDecoder {

    public CellocatorProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    static final int MSG_CLIENT_STATUS = 0;
    static final int MSG_CLIENT_PROGRAMMING = 3;
    static final int MSG_CLIENT_SERIAL_LOG = 7;
    static final int MSG_CLIENT_SERIAL = 8;
    static final int MSG_CLIENT_MODULAR = 9;
    static final int MSG_CLIENT_MODULAR_EXT = 11;

    public static final int MSG_SERVER_ACKNOWLEDGE = 4;

    public static ByteBuf encodeContent(int type, int uniqueId, int packetNumber, ByteBuf content) {

        ByteBuf buf = Unpooled.buffer();
        buf.writeByte('M');
        buf.writeByte('C');
        buf.writeByte('G');
        buf.writeByte('P');
        buf.writeByte(type);
        buf.writeIntLE(uniqueId);
        buf.writeByte(packetNumber);
        buf.writeIntLE(0); // authentication code
        buf.writeBytes(content);

        byte checksum = 0;
        for (int i = 4; i < buf.writerIndex(); i++) {
            checksum += buf.getByte(i);
        }
        buf.writeByte(checksum);

        return buf;
    }

    private void sendResponse(Channel channel, SocketAddress remoteAddress, long deviceId, byte packetNumber) {
        if (channel != null) {
            ByteBuf content = Unpooled.buffer();
            content.writeByte(0);
            content.writeByte(packetNumber);
            content.writeZero(11);

            ByteBuf reply = encodeContent(MSG_SERVER_ACKNOWLEDGE, (int) deviceId, 0, content);
            channel.writeAndFlush(new NetworkMessage(reply, remoteAddress));
        }
    }

    private void sendModuleResponse(Channel channel, SocketAddress remoteAddress, long deviceId, byte packetNumber) {
        if (channel != null) {
            ByteBuf content = Unpooled.buffer();
            content.writeByte(0x80);
            content.writeShortLE(10); // modules length
            content.writeIntLE(0); // reserved
            content.writeByte(9); // ack module type
            content.writeShortLE(3); // module length
            content.writeByte(0); // ack
            content.writeShortLE(0); // reserved

            ByteBuf reply = encodeContent(MSG_CLIENT_MODULAR_EXT, (int) deviceId, packetNumber, content);
            channel.writeAndFlush(new NetworkMessage(reply, remoteAddress));
        }
    }

    private String decodeAlarm(short reason) {
        switch (reason) {
            case 4:
                return Position.ALARM_EMERGENCY;
            case 6:
                return Position.ALARM_ENGINE_ACTIVATED;
            case 8:
                return Position.ALARM_TOW;
            case 9:
                return Position.ALARM_ROBBERY;
            case 11:
                return Position.ALARM_IDLE;
            case 13:
                return Position.ALARM_KEYPAD_LOCKED;
            case 15:
                return Position.ALARM_CRASH;
            case 16:
                return Position.ALARM_FUNCTION_BUTTON_PRESSED;
            case 18:
                return Position.ALARM_VOICE_CALL;
            case 19:
                return Position.ALARM_LOCK;
            case 21:
                return Position.ALARM_COASTING_DETECTION;
            case 22:
                return Position.ALARM_VIOLATION_1_FREQUENCY;
            case 23:
                return Position.ALARM_VIOLATION_2_FREQUENCY;
            case 25:
                return Position.ALARM_SPEED_IGNITION_OFF;
            case 31:
                return Position.ALARM_REPLY_COMMAND;
            case 32:
                return Position.ALARM_IP_CHANGED;
            case 33:
                return Position.ALARM_GPS_NAVIGATION_START;
            case 34:
                return Position.ALARM_OVER_SPEED_START;
            case 35:
                return Position.ALARM_IDLE_SPEED_START;
            case 38:
                return Position.ALARM_GPS_FACTORY_RESET;
            case 41:
                return Position.ALARM_GPS_NAVIGATION_END;
            case 42:
                return Position.ALARM_OVER_SPEED_END;
            case 43:
                return Position.ALARM_IDLE_SPEED_END;
            case 44:
                return Position.ALARM_TIMED_EVENT;
            case 46:
                return Position.ALARM_DIVER_AUTHENTICATION_UPDATE;
            case 47:
                return Position.ALARM_DRIVING_WITHOUT_AUTHENTICATION;
            case 48:
                return Position.ALARM_DOOR_CLOSE;
            case 49:
                return Position.ALARM_SHOCK_UNLOCK2_INACTIVE;
            case 50:
                return Position.ALARM_CFE_INPUT6_INACTIVE;
            case 51:
                return Position.ALARM_VOLUME_SENSOR_INACTIVE_EVENT;
            case 53:
                return Position.ALARM_DRIVING_STOP;
            case 54:
                return Position.ALARM_DISTRESS_BUTTON_INACTIVE;
            case 55:
                return Position.ALARM_UNLOCK_INPUT_INACTIVE;
            case 57:
                return Position.ALARM_CFE_INPUT1_INACTIVE;
            case 58:
                return Position.ALARM_LOCK_INPUT_INACTIVE;
            case 59:
                return Position.ALARM_CFE_INPUT2_INACTIVE;
            case 60:
                return Position.ALARM_CFE_INPUT3_INACTIVE;
            case 61:
                return Position.ALARM_CFE_INPUT4_INACTIVE;
            case 62:
                return Position.ALARM_CFE_INPUT5_INACTIVE;
            case 63:
                return Position.ALARM_IGNITION_INPUT_INACTIVE;
            case 64:
                return Position.ALARM_DOOR_OPEN;
            case 65:
                return Position.ALARM_SHOCK_UNLOCK2_ACTIVE;
            case 66:
                return Position.ALARM_CFE_INPUT6_ACTIVE;
            case 67:
                return Position.ALARM_VOLUME_SENSOR_ACTIVE;
            case 69:
                return Position.ALARM_DRIVING_START;
            case 70:
                return Position.ALARM_SOS;
            case 71:
                return Position.ALARM_UNLOCK_INPUT_ACTIVE;
            case 73:
                return Position.ALARM_CFE_INPUT1_ACTIVE;
            case 74:
                return Position.ALARM_LOCK_INPUT_ACTIVE;
            case 75:
                return Position.ALARM_CFE_INPUT2_ACTIVE;
            case 76:
                return Position.ALARM_CFE_INPUT3_ACTIVE;
            case 77:
                return Position.ALARM_CFE_INPUT4_ACTIVE;
            case 78:
                return Position.ALARM_CFE_INPUT5_ACTIVE;
            case 79:
                return Position.ALARM_IGNITION_INPUT_ACTIVE;
            case 80:
                return Position.ALARM_POWER_CUT;
            case 81:
                return Position.ALARM_LOW_POWER;
            case 82:
                return Position.ALARM_BACKUP_BATTERY_DISCONNECTED;
            case 83:
                return Position.ALARM_BACKUP_BATTERY_LOW_LEVEL;
            case 84:
                return Position.ALARM_HALT_MOVEMENT_END;
            case 85:
                return Position.ALARM_GO_MOVEMENT_START;
            case 87:
                return Position.ALARM_MAIN_POWER_CONNECTED;
            case 88:
                return Position.ALARM_MAIN_POWER_HIGH_LEVEL;
            case 89:
                return Position.ALARM_BACKUP_BATTERY_CONNECTED;
            case 90:
                return Position.ALARM_BACKUP_BATTERY_HIGH_LEVEL;
            case 91:
                return Position.ALARM_MESSAGE_FROM_KEYBOARD;
            case 92:
                return Position.ALARM_SATELLITE_COMMUNICATION;
            case 99:
                return Position.ALARM_HARSH_BRAKING_SENSOR;
            case 100:
                return Position.ALARM_SUDDEN_COURSE_CHANGE_SENSOR;
            case 101:
                return Position.ALARM_HARSH_ACCELERATION_SENSOR;
            case 154:
                return Position.ALARM_MAIN_POWER_LOW;
            case 158:
                return Position.ALARM_TAMPER_ACTIVE;
            case 159:
                return Position.ALARM_TAMPER_INACTIVE;
            case 160:
                return Position.ALARM_CFE_VENT;
            case 161:
                return Position.ALARM_UNLOCK_INPUT;
            case 166:
                return Position.ALARM_ORIENTATION_CHANGE;
            case 167:
                return Position.ALARM_CAN_GPS_SPEED_CALIBRATION;
            case 190:
                return Position.ALARM_NO_MODEM_ZONE_ENTRY;
            case 191:
                return Position.ALARM_GEO_HOTSPOT_VIOLATION;
            case 192:
                return Position.ALARM_FREQUENCY_MEASUREMENT_THRESHOLD_VIOLATION;
            case 194:
                return Position.ALARM_ANALOG_MEASUREMENT_THRESHOLD_VIOLATION;
            case 199:
                return Position.ALARM_TRAILER_CONNECTION_STATUS;
            case 200:
                return Position.ALARM_AHR;
            case 201:
                return Position.ALARM_PSP;
            case 202:
                return Position.ALARM_WAKE_UP_EVENT;
            case 203:
                return Position.ALARM_PRE_HIBERNATION_EVENT;
            case 204:
                return Position.ALARM_VECTOR_CHANGE_CURVE_SMOOTHING;
            case 205:
                return Position.ALARM_GARMIN_CONNECTION_STATUS;
            case 206:
                return Position.ALARM_JAMMING;
            case 207:
                return Position.ALARM_RADIO_OFF_MODE;
            case 208:
                return Position.ALARM_HEADER_ERROR;
            case 212:
                return Position.ALARM_GEO_FENCE_OVER_SPEED_START;
            case 213:
                return Position.ALARM_GEO_FENCE_OVER_SPEED_END;
            case 222:
                return Position.ALARM_POINTERCEPT_BEACON_START_STOP;
            case 223:
                return Position.ALARM_POINTERCEPT_CPIN_ERROR;
            case 224:
                return Position.ALARM_OTA;
            case 225:
                return Position.ALARM_POINTERCEPT_PERIODIC_BEACON_TRANSMISSION;
            case 247:
                return Position.ALARM_FINISH_MODE;
            case 252:
                return Position.ALARM_COM_LOCATION_GLANCING;
            case 253:
                return Position.ALARM_VIOLATION_KEEP_IN_FENCE;
            case 254:
                return Position.ALARM_VIOLATION_KEEP_OUT_FENCE;
            case 255:
                return Position.ALARM_VIOLATION_WAYPOINT;
            default:
                return null;
        }
    }

    private String decodeReason(int reason) {
        switch (reason) {
            case 0:
                return "Reserved";
            case 1:
                return "IP Up";
            case 2:
                return "Crash";
            case 3:
                return "Crash Data Ready to Upload";
            case 4:
                return "Maneuver Memory Overflow";
            case 5:
                return "Calibration";
            case 6:
                return "Harsh Acceleration";
            case 7:
                return "Harsh Braking";
            case 8:
                return "Harsh Turn";
            case 9:
                return "Speeding";
            case 10:
                return "Lane Crossing";
            case 11:
                return "Off Road";
            case 12:
                return "Excessive RPM";
            case 13:
                return "Idling";
            case 14:
                return "Reply to Command";
            case 15:
                return "Turn & Brake";
            case 16:
                return "Turn & Accelerate";
            case 17:
                return "File Upload";
            case 18:
                return "Ignition status";
            case 19:
                return "Go/Halt Event";
            case 20:
                return "Driver ID Update";
            case 24:
                return "Trip Start/Stop";
            case 25:
                return "Speeding - GPS Recording";
            case 26:
                return "Off Road Setup Process";
            case 27:
                return "DFD Communication Status Change";
            default:
                return null;
        }
    }

    private Position decodeCSA(ByteBuf buf, DeviceSession deviceSession, int numerator) {

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());
        position.set(Position.KEY_TYPE, 11);
        position.set("numerator", numerator);

        while (buf.readableBytes() > 3) { //checksum

            int moduleType = buf.readUnsignedByte();
            int endIndex = buf.readUnsignedByte() + buf.readerIndex();

            switch (moduleType) {
                case 8:
                    int featuresId = buf.readUnsignedByte();
                    position.set("features",featuresId == 0 ? "IQ50": "IQ40" );
                    position.set("csaProtocol",buf.readUnsignedByte());
                    position.set("idLsb",buf.readUnsignedByte());
                    position.set("idMsb",buf.readUnsignedByte());
                    position.set("fwVersion",buf.readUnsignedByte());
                    position.set("fwSubVersion",buf.readUnsignedByte());
                    position.set("spare11",buf.readUnsignedByte());
                    position.set("spare12",buf.readUnsignedByte());
                    position.set("spare13",buf.readUnsignedByte());
                    position.set("spare14",buf.readUnsignedByte());
                    position.set("spare15",buf.readUnsignedByte());
                    break;
                case 30:
                    int reasonCode = buf.readUnsignedByte();
                    String reason = decodeReason(reasonCode);
                    position.set("reason",reason);

                    position.set("subReasonValue",buf.readByte());
                    position.set("numerator2",buf.readUnsignedShort());
                    position.set("operational",buf.readUnsignedByte());
                    position.set("spare",buf.readUnsignedByte());
                    position.set("reason1",buf.readUnsignedByte());
                    position.set("reason2",buf.readUnsignedByte());
                    position.set("reason3",buf.readUnsignedByte());
                    position.set("reason4",buf.readUnsignedByte());
                    position.set("reason5",buf.readUnsignedByte());
                    position.set("reason6",buf.readUnsignedByte());
                    position.set("trip",buf.readUnsignedMediumLE());
                    position.set("maneuver",buf.readUnsignedMediumLE());
                    position.set("maneuvers",buf.readUnsignedByte());
                    position.set("accident",buf.readUnsignedByte());
                    position.set("spare2",buf.readUnsignedByte());
                    position.set("hdop",buf.readUnsignedByte());
                    position.set("mode1",buf.readUnsignedByte());
                    position.set("mode2",buf.readUnsignedByte());
                    position.set("satellites",buf.readUnsignedByte());

                    position.setLongitude(buf.readIntLE() / Math.PI * 180 / 100000000);
                    position.setLatitude(buf.readIntLE() / Math.PI * 180 / 100000000);

                    position.setAltitude(buf.readIntLE() * 0.01);

                    position.setSpeed(buf.readUnsignedByte());
                    position.setCourse(buf.readUnsignedShortLE() / Math.PI * 180.0 / 1000.0);

                    DateBuilder dateBuilder = new DateBuilder()
                            .setTimeReverse(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte())
                            .setDateReverse(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());
                    position.setTime(dateBuilder.getDate());

                    break;
                case 32:

                    position.set("trip2",buf.readUnsignedMediumLE());
                    position.set("driver1",buf.readUnsignedByte());
                    position.set("driver2",buf.readUnsignedByte());
                    position.set("driver3",buf.readUnsignedByte());
                    position.set("driver4",buf.readUnsignedByte());
                    position.set("driver5",buf.readUnsignedByte());
                    position.set("driver6",buf.readUnsignedByte());

                    int date1 = buf.readUnsignedByte();
                    int date2 = buf.readUnsignedByte();
                    int date3 = buf.readUnsignedByte();
                    int date4 = buf.readUnsignedByte();
                    int date5 = buf.readUnsignedByte();
                    int date6 = buf.readUnsignedByte();
                    int date7 = buf.readUnsignedByte();

                    DateBuilder startDateBuilder = new DateBuilder()
                            .setTimeReverse(date2, date3, date4)
                            .setDateReverse(date5, date6, date7);
                    position.set("startDate",startDateBuilder.getDate().toString());
                    double tripDuration = buf.readUnsignedMediumLE() / 60.00;
                    position.set("tripDuration",tripDuration);
                    position.set("distanceTraveled",buf.readUnsignedIntLE());
                    double movement = buf.readUnsignedIntLE() / 60.00;
                    position.set("movement",movement);
                    position.set("idleShort",buf.readUnsignedIntLE());
                    position.set("idleLong",buf.readUnsignedIntLE());

                    position.set("x",buf.readUnsignedShort());
                    position.set("y",buf.readUnsignedShort());
                    position.set("z",buf.readUnsignedShort());
                    position.set("rms",buf.readUnsignedIntLE());

                    /*
                    //TODO: ESTOS FALLAN
                    int x = buf.readUnsignedShort();
                    int y = buf.readUnsignedShort();
                    int z = buf.readUnsignedShort();
                    long rms = buf.readUnsignedIntLE();
                     */

                    position.set("speedMax",buf.readUnsignedByte());
                    position.set("speedAverage",buf.readUnsignedByte());
                    position.set("tripStart",buf.readUnsignedByte());
                    position.set("tripEnd",buf.readUnsignedByte());
                    position.set("weightedSafety",buf.readUnsignedByte());
                    position.set("weightedEco",buf.readUnsignedByte());
                    position.set("ecoIdle",buf.readUnsignedByte());
                    position.set("ecoUrban",buf.readUnsignedByte());
                    position.set("ecoHighway",buf.readUnsignedByte());
                    position.set("ecoIdleTime",buf.readUnsignedMediumLE());
                    position.set("ecoUrbanTime",buf.readUnsignedMediumLE());
                    position.set("ecoHighwayTime",buf.readUnsignedMediumLE());
                    position.set("tripOdometer",buf.readUnsignedIntLE());
                    position.set("tripDelta",buf.readUnsignedShort());
                    position.set("tripMovement",buf.readUnsignedShort());
                    position.set("tripIdle",buf.readUnsignedShort());

                    break;
                default:
                    break;
            }
            buf.readerIndex(endIndex);
        }

        return position;
    }

    private Position decodeStatus(ByteBuf buf, DeviceSession deviceSession, boolean alternative) {

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());
        position.set(Position.KEY_TYPE, 0);

        position.set(Position.KEY_VERSION_HW, buf.readUnsignedByte());
        position.set(Position.KEY_VERSION_FW, buf.readUnsignedByte());
        buf.readUnsignedByte(); // protocol version

        position.set(Position.KEY_STATUS, buf.readUnsignedByte() & 0x0f);

        buf.readUnsignedByte(); // operator / configuration flags
        buf.readUnsignedByte(); // reason data
        position.set(Position.KEY_ALARM, decodeAlarm(buf.readUnsignedByte()));

        position.set("mode", buf.readUnsignedByte());

        long input = buf.readUnsignedIntLE();
        position.set(Position.KEY_DOOR, BitUtil.check(input, 3 * 8));
        position.set(Position.KEY_IGNITION, BitUtil.check(input, 2 * 8 + 7));
        position.set(Position.KEY_CHARGE, BitUtil.check(input, 7));
        position.set(Position.KEY_INPUT, input);

        if (alternative) {
            buf.readUnsignedByte(); // input
            position.set(Position.KEY_BATTERY_LEVEL, buf.readUnsignedByte() * 0.1176470588235);
            position.set(Position.PREFIX_ADC + 1, buf.readUnsignedByte());
            buf.readByte();
            position.set(Position.PREFIX_FUEL_LEVEL, ((buf.readUnsignedByte() * 0.0098) * 100) / 2.5 );
        } else {
            buf.readUnsignedByte(); // operator
            position.set(Position.PREFIX_ADC + 1, buf.readUnsignedByte());
            position.set(Position.PREFIX_ADC + 2, buf.readUnsignedByte());
            position.set(Position.PREFIX_ADC + 3, buf.readUnsignedByte());
            position.set(Position.PREFIX_ADC + 4, buf.readUnsignedByte());
        }

        position.set(Position.KEY_ODOMETER, buf.readUnsignedMediumLE());

        buf.skipBytes(6); // multi-purpose data
        buf.readUnsignedShortLE(); // fix time
        buf.readUnsignedByte(); // location status
        buf.readUnsignedByte(); // mode 1
        buf.readUnsignedByte(); // mode 2

        position.set(Position.KEY_SATELLITES, buf.readUnsignedByte());

        position.setValid(true);

        if (alternative) {
            position.setLongitude(buf.readIntLE() / 10000000.0);
            position.setLatitude(buf.readIntLE() / 10000000.0);
        } else {
            position.setLongitude(buf.readIntLE() / Math.PI * 180 / 100000000);
            position.setLatitude(buf.readIntLE() / Math.PI * 180 / 100000000);
        }

        if(position.getLatitude() == 0 && position.getLongitude() == 0){
            position.setValid(false);
        }

        position.setAltitude(buf.readIntLE() * 0.01);

        if (alternative) {
            position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedIntLE()));
            position.setCourse(buf.readUnsignedShortLE() / 1000.0);
        } else {
            position.setSpeed(UnitsConverter.knotsFromMps(buf.readUnsignedIntLE() * 0.01));
            position.setCourse(buf.readUnsignedShortLE() / Math.PI * 180.0 / 1000.0);
        }

        DateBuilder dateBuilder = new DateBuilder()
                .setTimeReverse(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte())
                .setDateReverse(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedShortLE());
        position.setTime(dateBuilder.getDate());

        return position;
    }

    private Position decodeModular(ByteBuf buf, DeviceSession deviceSession) {

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());
        position.set(Position.KEY_TYPE, 11 );

        buf.readUnsignedByte(); // packet control
        buf.readUnsignedShortLE(); // length
        buf.readUnsignedShortLE(); // reserved
        buf.readUnsignedShortLE(); // reserved

        while (buf.readableBytes() > 3) {

            int moduleType = buf.readUnsignedByte();
            int endIndex = buf.readUnsignedShortLE() + buf.readerIndex();

            switch (moduleType) {
                case 1:
                    StringBuilder error = new StringBuilder();
                    int countMode3 = buf.readUnsignedByte(); // Number of DTCs mode 3
                    int countMode7 = buf.readUnsignedByte(); // Number of DTCs mode 7
                    error.append("DTC ");
                    for (int i = 0; i < countMode3; i++) {

                        byte part1 = buf.readByte();
                        byte part2 = buf.readByte();

                        // get ErrorType
                        String partError = String.format("%8s", Integer.toBinaryString(part1 & 0xFF)).replace(' ','0');
                        String typeError = partError.substring(0,2);

                        // Remove 2 bits from ErrorType
                        part1 = (byte) (part1 & ~(1 << 6));
                        part1 = (byte) (part1 & ~(1 << 7));

                        // Value of code
                        int codeDTC = ((part1 & 0xff) << 8) | (part2 & 0xff);

                        String codeDTCHex = Integer.toHexString(codeDTC);

                        switch(typeError){
                            case "00":
                                error.append("mode3-Power train-");
                                break;
                            case "01":
                                error.append("mode3-Chassis-");
                                break;
                            case "10":
                                error.append("mode3-Body-");
                                break;
                            case "11":
                                error.append("mode3-Network-");
                                break;
                        }
                        error.append(codeDTCHex);
                        error.append("|");
                    }
                    for (int i = 0; i < countMode7; i++) {

                        byte part1 = buf.readByte();
                        byte part2 = buf.readByte();

                        // get ErrorType
                        String partError = String.format("%8s", Integer.toBinaryString(part1 & 0xFF)).replace(' ','0');
                        String typeError = partError.substring(0,2);

                        // Remove 2 bits from ErrorType
                        part1 = (byte) (part1 & ~(1 << 6));
                        part1 = (byte) (part1 & ~(1 << 7));

                        // Value of code
                        int codeDTC = ((part1 & 0xff) << 8) | (part2 & 0xff);

                        String codeDTCHex = Integer.toHexString(codeDTC);

                        switch(typeError){
                            case "00":
                                error.append("mode7-P–Power train-");
                                break;
                            case "01":
                                error.append("mode7-C-Chassis-");
                                break;
                            case "10":
                                error.append("mode7-B-Body-");
                                break;
                            case "11":
                                error.append("mode7-U–Network-");
                                break;
                        }
                        error.append(codeDTCHex);
                        error.append("|");
                    }
                    position.set(Position.KEY_DTCS , error.toString() );
                    position.setTime(new Date());
                    break;
                case 2:
                    buf.readUnsignedShortLE(); // operator id
                    String plSignature =  Long.toHexString(buf.readUnsignedInt()); // pl signature
                    int count = buf.readUnsignedByte();
                    for (int i = 0; i < count; i++) {
                        String varId = Integer.toHexString(buf.readUnsignedShortLE()); //VAR id
                        buf.readUnsignedByte(); // variable length
                        long payload = buf.readUnsignedIntLE(); // calc value
                        CanVariable canVariable = getCanVariable(plSignature,varId);
                        if(canVariable != null){
                            try{
                                long result = canVariable.getFwOffset() + payload * canVariable.getFwMultiplier()/ canVariable.getFwDivider();
                                position.set(canVariable.getTitle(), result );
                            }catch(Exception ex) {
                                position.set(Position.KEY_UNKNOWN + " " + varId, payload );
                                String err = ex.getMessage();
                            }
                        }else{
                            position.set(Position.KEY_ERROR + "-" + varId, payload );
                        }
                    }
                    break;
                case 6:
                    buf.readUnsignedByte(); // hdop
                    buf.readUnsignedByte(); // mode 1
                    buf.readUnsignedByte(); // mode 2
                    buf.readUnsignedByte(); // satellites
                    position.setLongitude(buf.readIntLE() / Math.PI * 180 / 100000000);
                    position.setLatitude(buf.readIntLE() / Math.PI * 180 / 100000000);
                    position.setAltitude(buf.readIntLE() * 0.01);
                    position.setSpeed(UnitsConverter.knotsFromMps(buf.readUnsignedByte() * 0.01));
                    position.setCourse(buf.readUnsignedShortLE() / Math.PI * 180.0 / 1000.0);
                    break;
                case 7:
                    buf.readUnsignedByte(); // valid
                    DateBuilder dateBuilder = new DateBuilder()
                            .setTimeReverse(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte())
                            .setDateReverse(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());
                    position.setTime(dateBuilder.getDate());
                    break;
                default:
                    break;
            }

            buf.readerIndex(endIndex);

        }

        return position;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        if(buf.getByte(buf.readerIndex()) == 'C' && buf.getByte(buf.readerIndex() + 1) == 'S' && buf.getByte(buf.readerIndex() + 2) == 'A'){
            buf.skipBytes(5); // system code and lenght
            int numerator = buf.readUnsignedShortLE();
            int packet = buf.readUnsignedByte();
            long deviceUniqueId = buf.readUnsignedIntLE();
            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, String.valueOf(deviceUniqueId));
            if (deviceSession == null) {
                return null;
            }
            return decodeCSA(buf, deviceSession, numerator);
        }else {
            boolean alternative = buf.getByte(buf.readerIndex() + 3) != 'P';

            buf.skipBytes(4); // system code
            int type = buf.readUnsignedByte();

            long deviceUniqueId = buf.readUnsignedIntLE();
            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, String.valueOf(deviceUniqueId));
            if (deviceSession == null) {
                return null;
            }

            if (type != MSG_CLIENT_SERIAL) {
                buf.readUnsignedShortLE(); // communication control
            }
            byte packetNumber = buf.readByte();

            if (type == MSG_CLIENT_MODULAR_EXT) {
                sendModuleResponse(channel, remoteAddress, deviceUniqueId, packetNumber);
            } else {
                sendResponse(channel, remoteAddress, deviceUniqueId, packetNumber);
            }

            if (type == MSG_CLIENT_STATUS) {
                return decodeStatus(buf, deviceSession, alternative);
            } else if (type == MSG_CLIENT_MODULAR_EXT) {
                return decodeModular(buf, deviceSession);
            }

            return null;
        }
    }

}
