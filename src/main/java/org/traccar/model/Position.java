/*
 * Copyright 2012 - 2016 Anton Tananaev (anton@traccar.org)
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
package org.traccar.model;

import java.util.Date;

import org.traccar.database.QueryIgnore;

public class Position extends Message {

    public static final String KEY_ORIGINAL = "raw";
    public static final String KEY_INDEX = "index";
    public static final String KEY_HDOP = "hdop";
    public static final String KEY_VDOP = "vdop";
    public static final String KEY_PDOP = "pdop";
    public static final String KEY_SATELLITES = "sat"; // in use
    public static final String KEY_SATELLITES_VISIBLE = "satVisible";
    public static final String KEY_RSSI = "rssi";
    public static final String KEY_GPS = "gps";
    public static final String KEY_ROAMING = "roaming";
    public static final String KEY_EVENT = "event";
    public static final String KEY_ALARM = "alarm";
    public static final String KEY_STATUS = "status";
    public static final String KEY_ODOMETER = "odometer"; // meters
    public static final String KEY_ODOMETER_SERVICE = "serviceOdometer"; // meters
    public static final String KEY_ODOMETER_TRIP = "tripOdometer"; // meters
    public static final String KEY_HOURS = "hours";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_HEART_RATE = "heartRate";
    public static final String KEY_INPUT = "input";
    public static final String KEY_OUTPUT = "output";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_AUDIO = "audio";

    // The units for the below four KEYs currently vary.
    // The preferred units of measure are specified in the comment for each.
    public static final String KEY_POWER = "power"; // volts
    public static final String KEY_BATTERY = "battery"; // volts
    public static final String KEY_BATTERY_LEVEL = "batteryLevel"; // percentage
    public static final String KEY_FUEL_LEVEL = "fuel"; // liters
    public static final String KEY_FUEL_USED = "fuelUsed"; // liters
    public static final String KEY_FUEL_CONSUMPTION = "fuelConsumption"; // liters/hour

    public static final String KEY_VERSION_FW = "versionFw";
    public static final String KEY_VERSION_HW = "versionHw";
    public static final String KEY_TYPE = "type";
    public static final String KEY_IGNITION = "ignition";
    public static final String KEY_FLAGS = "flags";
    public static final String KEY_ANTENNA = "antenna";
    public static final String KEY_CHARGE = "charge";
    public static final String KEY_IP = "ip";
    public static final String KEY_ARCHIVE = "archive";
    public static final String KEY_DISTANCE = "distance"; // meters
    public static final String KEY_TOTAL_DISTANCE = "totalDistance"; // meters
    public static final String KEY_RPM = "rpm";
    public static final String KEY_VIN = "vin";
    public static final String KEY_APPROXIMATE = "approximate";
    public static final String KEY_THROTTLE = "throttle";
    public static final String KEY_MOTION = "motion";
    public static final String KEY_ARMED = "armed";
    public static final String KEY_GEOFENCE = "geofence";
    public static final String KEY_ACCELERATION = "acceleration";
    public static final String KEY_DEVICE_TEMP = "deviceTemp"; // celsius
    public static final String KEY_COOLANT_TEMP = "coolantTemp"; // celsius
    public static final String KEY_ENGINE_LOAD = "engineLoad";
    public static final String KEY_OPERATOR = "operator";
    public static final String KEY_COMMAND = "command";
    public static final String KEY_BLOCKED = "blocked";
    public static final String KEY_DOOR = "door";
    public static final String KEY_AXLE_WEIGHT = "axleWeight";
    public static final String KEY_G_SENSOR = "gSensor";
    public static final String KEY_ICCID = "iccid";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_SPEED_LIMIT = "speedLimit";

    public static final String KEY_DTCS = "dtcs";
    public static final String KEY_OBD_SPEED = "obdSpeed"; // knots
    public static final String KEY_OBD_ODOMETER = "obdOdometer"; // meters

    public static final String KEY_RESULT = "result";

    public static final String KEY_DRIVER_UNIQUE_ID = "driverUniqueId";

    //region new keys MARX
    public static final String KEY_IN_STATE = "inState";
    public static final String KEY_OUT_STATE = "outState";
    public static final String KEY_MODE = "mode";
    public static final String KEY_STT_RPT_TYPE = "sttRptType";
    public static final String KEY_MSG_NUM = "msgNum";
    public static final String KEY_BCK_VOLT = "bckVolt";
    public static final String KEY_ADC = "adc";
    public static final String KEY_DIST = "dist";
    public static final String KEY_PWR_VOLT = "pwrVolt";
    public static final String KEY_TOTAL_USED_FUEL = "totalUsedFuel";
    public static final String KEY_VEH_SPD = "vehSpd";
    public static final String KEY_IDLE_FUEL = "idleFuel";
    public static final String KEY_ENGINE_TEMPERATURE = "engineTemperature";
    public static final String KEY_OIL_PRESSURE = "oilPressure";
    public static final String KEY_ENGINE_RPM = "engineRpm";
    public static final String KEY_TOTAL_ENG_HR = "totalEngHr";
    public static final String KEY_VEH_PROT = "vehProt";
    public static final String KEY_DTC = "dtc";
    public static final String KEY_ENGINE_IDLE_TIME = "engineIdleTime";
    public static final String KEY_FUEL_ECONOMY = "fuelEconomy";
    public static final String KEY_TRIP_FUEL = "tripFuel";
    public static final String KEY_ALERT_ID = "alertId";
    public static final String KEY_ALERT_MOD = "alertMod";
    public static final String KEY_ALERT_DATA = "alertData";
    public static final String KEY_LEN = "len";
    public static final String KEY_DATA = "data";
    public static final String KEY_LAT_TRV_ST = "latTrvSt";
    public static final String KEY_LON_TRV_ST = "lonTrvSt";
    public static final String KEY_LAT_TRV_FI = "latTrvFi";
    public static final String KEY_LON_TRV_FI = "lonTrvFi";
    public static final String KEY_DRIVER_ID = "driverId";
    public static final String KEY_TRV_DIST = "trvDist";
    public static final String KEY_TRV_H_METER = "trvHMeter";
    public static final String KEY_IDLE_TIME = "idleTime";
    public static final String KEY_IDLE_EVENTS = "idleEvents";
    public static final String KEY_SPD_TIME = "spdTime";
    public static final String KEY_SPD_EVENTS = "spdEvents";
    public static final String KEY_MAX_SPD = "maxSpd";
    public static final String KEY_AVG_SPD = "avgSpd";
    public static final String KEY_GPS_ODOM = "gpsOdom";
    public static final String KEY_SPEED_HISTOGRAM = "speedHistogram";
    //endregion

    // Start with 1 not 0
    public static final String PREFIX_TEMP = "temp";
    public static final String PREFIX_ADC = "adc";
    public static final String PREFIX_IO = "io";
    public static final String PREFIX_COUNT = "count";
    public static final String PREFIX_IN = "in";
    public static final String PREFIX_OUT = "out";

    public static final String ALARM_GENERAL = "general";
    public static final String ALARM_SOS = "sos";
    public static final String ALARM_VIBRATION = "vibration";
    public static final String ALARM_MOVEMENT = "movement";
    public static final String ALARM_LOW_SPEED = "lowspeed";
    public static final String ALARM_OVERSPEED = "overspeed";
    public static final String ALARM_FALL_DOWN = "fallDown";
    public static final String ALARM_LOW_POWER = "lowPower";
    public static final String ALARM_LOW_BATTERY = "lowBattery";
    public static final String ALARM_FAULT = "fault";
    public static final String ALARM_POWER_OFF = "powerOff";
    public static final String ALARM_POWER_ON = "powerOn";
    public static final String ALARM_DOOR = "door";
    public static final String ALARM_LOCK = "lock";
    public static final String ALARM_UNLOCK = "unlock";
    public static final String ALARM_GEOFENCE = "geofence";
    public static final String ALARM_GEOFENCE_ENTER = "geofenceEnter";
    public static final String ALARM_GEOFENCE_EXIT = "geofenceExit";
    public static final String ALARM_GPS_ANTENNA_CUT = "gpsAntennaCut";
    public static final String ALARM_ACCIDENT = "accident";
    public static final String ALARM_TOW = "tow";
    public static final String ALARM_IDLE = "idle";
    public static final String ALARM_HIGH_RPM = "highRpm";
    public static final String ALARM_ACCELERATION = "hardAcceleration";
    public static final String ALARM_BRAKING = "hardBraking";
    public static final String ALARM_CORNERING = "hardCornering";
    public static final String ALARM_LANE_CHANGE = "laneChange";
    public static final String ALARM_FATIGUE_DRIVING = "fatigueDriving";
    public static final String ALARM_POWER_CUT = "powerCut";
    public static final String ALARM_POWER_RESTORED = "powerRestored";
    public static final String ALARM_JAMMING = "jamming";
    public static final String ALARM_TEMPERATURE = "temperature";
    public static final String ALARM_PARKING = "parking";
    public static final String ALARM_SHOCK = "shock";
    public static final String ALARM_BONNET = "bonnet";
    public static final String ALARM_FOOT_BRAKE = "footBrake";
    public static final String ALARM_FUEL_LEAK = "fuelLeak";
    public static final String ALARM_TAMPERING = "tampering";
    public static final String ALARM_REMOVING = "removing";

    public static final String ALARM_EMERGENCY = "emergency";
    public static final String ALARM_ENGINE_ACTIVATED = "engineActivated";
    public static final String ALARM_ROBBERY = "robbery";
    public static final String ALARM_KEYPAD_LOCKED = "keypadLoked";
    public static final String ALARM_CRASH = "crash";
    public static final String ALARM_FUNCTION_BUTTON_PRESSED = "functionButtonPressed";
    public static final String ALARM_VOICE_CALL = "voiceCall";
    public static final String ALARM_COASTING_DETECTION = "coastingDetection";
    public static final String ALARM_VIOLATION_1_FREQUENCY = "violation1Frequency";
    public static final String ALARM_VIOLATION_2_FREQUENCY = "violation2Frequency";
    public static final String ALARM_SPEED_IGNITION_OFF = "speedIgnitionOff";
    public static final String ALARM_REPLY_COMMAND = "replyCommand";
    public static final String ALARM_IP_CHANGED = "ipChanged";
    public static final String ALARM_GPS_NAVIGATION_START = "gpsNavigationStart";
    public static final String ALARM_OVER_SPEED_START = "overSpeedStart";
    public static final String ALARM_IDLE_SPEED_START = "idleSpeedStart";
    public static final String ALARM_GPS_FACTORY_RESET = "gpsFactoryReset";
    public static final String ALARM_GPS_NAVIGATION_END = "gpsNavigationEnd";
    public static final String ALARM_OVER_SPEED_END = "overSpeedEnd";
    public static final String ALARM_IDLE_SPEED_END = "idleSpeedEnd";
    public static final String ALARM_TIMED_EVENT = "timedEvent";
    public static final String ALARM_DIVER_AUTHENTICATION_UPDATE = "driverAuthenticationUpdate";
    public static final String ALARM_DRIVING_WITHOUT_AUTHENTICATION = "drivingWithoutAuthentication";
    public static final String ALARM_DOOR_CLOSE = "doorClose";
    public static final String ALARM_SHOCK_UNLOCK2_INACTIVE = "shockUnlock2Inactive";
    public static final String ALARM_CFE_INPUT6_INACTIVE = "cfeInput6Inactive";
    public static final String ALARM_VOLUME_SENSOR_INACTIVE_EVENT = "volumeSensorInactiveEvent";
    public static final String ALARM_DRIVING_STOP = "drivingStop";
    public static final String ALARM_DISTRESS_BUTTON_INACTIVE = "distressButtonInactive";
    public static final String ALARM_UNLOCK_INPUT_INACTIVE = "unlockInputInactive";
    public static final String ALARM_CFE_INPUT1_INACTIVE = "cfeInput1Inactive";
    public static final String ALARM_LOCK_INPUT_INACTIVE = "lockInputInactive";
    public static final String ALARM_CFE_INPUT2_INACTIVE = "cfeInput2Inactive";
    public static final String ALARM_CFE_INPUT3_INACTIVE = "cfeInput3Inactive";
    public static final String ALARM_CFE_INPUT4_INACTIVE = "cfeInput4Inactive";
    public static final String ALARM_CFE_INPUT5_INACTIVE = "cfeInput5Inactive";
    public static final String ALARM_IGNITION_INPUT_INACTIVE = "ignitionInputInactive";
    public static final String ALARM_DOOR_OPEN = "doorOpen";
    public static final String ALARM_SHOCK_UNLOCK2_ACTIVE = "shockUnlock2Active";
    public static final String ALARM_CFE_INPUT6_ACTIVE = "cfeInput6Active";
    public static final String ALARM_VOLUME_SENSOR_ACTIVE = "volumeSensorActive";
    public static final String ALARM_DRIVING_START = "drivingStart";
    public static final String ALARM_UNLOCK_INPUT_ACTIVE = "unlockInputActive";
    public static final String ALARM_CFE_INPUT1_ACTIVE = "cfeInput1Active";
    public static final String ALARM_LOCK_INPUT_ACTIVE = "lockInputActive";
    public static final String ALARM_CFE_INPUT2_ACTIVE = "cfeInput2Active";
    public static final String ALARM_CFE_INPUT3_ACTIVE = "cfeInput3Active";
    public static final String ALARM_CFE_INPUT4_ACTIVE = "cfeInput4Active";
    public static final String ALARM_CFE_INPUT5_ACTIVE = "cfeInput5Active";
    public static final String ALARM_IGNITION_INPUT_ACTIVE = "ignitionInputActive";
    public static final String ALARM_BACKUP_BATTERY_DISCONNECTED = "backupBatteryDisconnected";
    public static final String ALARM_BACKUP_BATTERY_LOW_LEVEL = "backupBatteryLowLevel";
    public static final String ALARM_HALT_MOVEMENT_END = "haltMovementEnd";
    public static final String ALARM_GO_MOVEMENT_START = "goMovementStart";
    public static final String ALARM_MAIN_POWER_CONNECTED = "mainPowerConnected";
    public static final String ALARM_MAIN_POWER_HIGH_LEVEL = "mainPowerHighLevel";
    public static final String ALARM_BACKUP_BATTERY_CONNECTED = "backupBatteryConnected";
    public static final String ALARM_BACKUP_BATTERY_HIGH_LEVEL = "backupBatteryHighLevel";
    public static final String ALARM_MESSAGE_FROM_KEYBOARD = "messageFromKeyboard";
    public static final String ALARM_SATELLITE_COMMUNICATION = "satelliteCommunication";
    public static final String ALARM_HARSH_BRAKING_SENSOR = "harshBrakingSensor";
    public static final String ALARM_SUDDEN_COURSE_CHANGE_SENSOR = "suddenCourseChangeSensor";
    public static final String ALARM_HARSH_ACCELERATION_SENSOR = "harshAccelerationSensor";
    public static final String ALARM_MAIN_POWER_LOW = "mainPowerLow";
    public static final String ALARM_TAMPER_ACTIVE = "tamperActive";
    public static final String ALARM_TAMPER_INACTIVE = "tamperInactive";
    public static final String ALARM_CFE_VENT = "cfeVent";
    public static final String ALARM_UNLOCK_INPUT = "unlockInput";
    public static final String ALARM_ORIENTATION_CHANGE = "orientationChange";
    public static final String ALARM_CAN_GPS_SPEED_CALIBRATION = "canGpsSpeedCalibration";
    public static final String ALARM_NO_MODEM_ZONE_ENTRY = "noModemZoneEntry";
    public static final String ALARM_GEO_HOTSPOT_VIOLATION = "geoHotspotViolation";
    public static final String ALARM_FREQUENCY_MEASUREMENT_THRESHOLD_VIOLATION = "frequencyMeasurementThresholdViolation";
    public static final String ALARM_ANALOG_MEASUREMENT_THRESHOLD_VIOLATION = "analogMeasurementThresholdViolation";
    public static final String ALARM_TRAILER_CONNECTION_STATUS = "trailerConnectionStatus";
    public static final String ALARM_AHR = "ahr";
    public static final String ALARM_PSP = "psp";
    public static final String ALARM_WAKE_UP_EVENT = "wakeUpEvent";
    public static final String ALARM_PRE_HIBERNATION_EVENT = "preHibernationEvent";
    public static final String ALARM_VECTOR_CHANGE_CURVE_SMOOTHING = "vectorChangeCurveSmoothing";
    public static final String ALARM_GARMIN_CONNECTION_STATUS = "garminConnectionStatus";
    public static final String ALARM_RADIO_OFF_MODE = "radioOffMode";
    public static final String ALARM_HEADER_ERROR = "headerError";
    public static final String ALARM_GEO_FENCE_OVER_SPEED_START = "geoFenceOverSpeedStart";
    public static final String ALARM_GEO_FENCE_OVER_SPEED_END = "geoFenceOverSpeedEnd";
    public static final String ALARM_POINTERCEPT_BEACON_START_STOP = "pointerceptBeaconStartStop";
    public static final String ALARM_POINTERCEPT_CPIN_ERROR = "pointerceptCpinError";
    public static final String ALARM_OTA = "ota";
    public static final String ALARM_POINTERCEPT_PERIODIC_BEACON_TRANSMISSION = "pointerceptPeriodicBeaconTransmission";
    public static final String ALARM_FINISH_MODE = "finishMode";
    public static final String ALARM_COM_LOCATION_GLANCING = "comLocationGlancing";
    public static final String ALARM_VIOLATION_KEEP_IN_FENCE = "violationKeepInFence";
    public static final String ALARM_VIOLATION_KEEP_OUT_FENCE = "violationKeepOutFence";
    public static final String ALARM_VIOLATION_WAYPOINT = "violationWaypoint";

    public static final String PREFIX_FUEL_LEVEL = "fuelLevel";

    //H02 Protocol MARX
    public static final String KEY_USR_ALARM = "usrAlarm";
    public static final String KEY_KEEP = "keep";
    public static final String KEY_GSM = "gsm";
    public static final String KEY_MILEAGE = "mileage";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_RECORD = "record";
    //endregion

    //Error plsignature MARX
    public static final String KEY_UNKNOWN = "unknown";
    public static final String KEY_ERROR = "error";
    //endregion

    //region Alarms from suntech MARX
    public static final String ALARM_UNDER_SPEED = "underSpeed";
    public static final String ALARM_GPS_DISCONNECTED = "gpsDisconnected";
    public static final String ALARM_GPS_CONNECTED = "gpsConnected";
    public static final String ALARM_GPS_SHORTED = "gpsShorted";
    public static final String ALARM_ENTER_SLEEP = "enterSleep";
    public static final String ALARM_EXIT_SLEEP = "exitSleep";
    public static final String ALARM_INPUT_HIGH = "inputHigh";
    public static final String ALARM_INPUT_LOW = "inputLow";
    public static final String ALARM_BATTERY_ERROR = "batteryError";
    public static final String ALARM_MOTION_DETECTED = "motionDetected";
    public static final String ALARM_IGNITION_ON = "ignitionOn";
    public static final String ALARM_IGNITION_OFF = "ignitionOff";
    public static final String ALARM_ENGINE_SPEED = "engineSpeed";
    public static final String ALARM_ENGINE_VIOLATION = "engineViolation";
    public static final String ALARM_ENGINE_TEMPERATURE_VIOLATION = "engineTemperatureViolation";
    public static final String ALARM_ENGINE_OIL_PRESSURE_VIOLATION = "engineOilPressureViolation";
    public static final String ALARM_ENGINE_RPM_VIOLATION = "engineRpmViolation";
    public static final String ALARM_ENGINE_ERROR_DTC = "engineErrorDtc";
    public static final String ALARM_ST25T_CONNECTED = "st25tConnected";
    public static final String ALARM_ST25T_DISCONNECTED = "st25tDisconnected";
    public static final String ALARM_FULL_NET_SCAN_FAIL = "fullNetScanFail";
    public static final String ALARM_FUEL_MAX_LEVEL_EXCESS = "fuelMaxLevelExcess";
    public static final String ALARM_FUEL_CONNECT = "fuelConnect";
    public static final String ALARM_FUEL_DISCONNECT = "fuelDisconnect";
    public static final String ALARM_FUEL_LEVEL_LOWER = "fuelLevelLower";
    public static final String ALARM_FUEL_LEVEL_UPPER = "fuelLevelUpper";
    public static final String ALARM_ST20U_CONNECT = "st20uConnect";
    public static final String ALARM_ST20U_DISCONNECT = "st20uDisconnect";
    public static final String ALARM_EXIT_POLYGEO_FENCE = "exitPolygeoFence";
    public static final String ALARM_ENTER_POLYGEO_FENCE = "enterPolygeoFence";
    public static final String ALARM_OVER_SPEED_POLYGEO_FENCE = "overSpeedPolygeoFence";
    public static final String ALARM_UNDER_SPEED_POLYGEO_FENCE = "underSpeedPolygeoFence";
    public static final String ALARM_SHARP_TURN_LEFT = "sharpTurnLeft";
    public static final String ALARM_SHARP_TURN_RIGHT = "sharpTurnRight";
    public static final String ALARM_DPA_CALIBRATED = "dpaCalibrated";
    public static final String ALARM_BUTTON_READ = "buttonRead";
    public static final String ALARM_ENTER_DRIVER_MODE = "enterDriverMode";
    public static final String ALARM_ENTER_PARK_MODE = "enterParkMode";
    public static final String ALARM_ENTER_IDLE_MODE = "enterIdleMode";
    public static final String ALARM_ENTER_SPEED_MODE = "enterSpeedMode";
    public static final String ALARM_EXCESS_IDLE = "excessIdle";
    public static final String ALARM_ENTER_TOW = "enterTow";
    public static final String ALARM_POWER_UP = "powerUp";
    public static final String ALARM_TEMP_HIGH = "tempHigh";
    public static final String ALARM_TEMP_LOW = "tempLow";
    public static final String ALARM_TEMP_RETURN = "tempReturn";
    public static final String ALARM_RFID_READ = "rfidRead";
    public static final String ALARM_SIM_CARD_REMOVED = "simCardRemoved";
    public static final String ALARM_GPS_FIX = "gpsFix";
    public static final String ALARM_RS232_ALERT = "rs232Alert";
    public static final String ALARM_MAGNET_DETECTED = "magnetDetected";
    public static final String ALARM_MAGNET_RELEASE = "magnetRelease";
    public static final String ALARM_POWER_KEY_PRESSED = "powerKeyPressed";
    public static final String ALARM_PANIC_BUTTON = "panicButton";
    public static final String ALARM_ANTI_THEFT = "antiTheft";
    public static final String ALARM_OBD_PARAMETER_SETTING = "odbParameterSetting";
    public static final String ALARM_MDSM7_EVENT = "mdsm7Event";
    public static final String ALARM_MDAS9_EVENT = "mdas9Event";
    public static final String ALARM_OVER_SPEED_CIRCULAR_GEO_FENCE = "overSpeedCircularGeoFence";
    public static final String ALARM_UNDER_SPEED_CIRCULAR_GEO_FENCE = "underSpeedCircularGeoFence";
    public static final String ALARM_TRAILER_EVENT = "trailerEvent";
    //endregion

    public Position() {
    }

    public Position(String protocol) {
        this.protocol = protocol;
        this.serverTime = new Date();
    }

    private String protocol;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private Date serverTime = new Date();

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    private Date deviceTime;

    public Date getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(Date deviceTime) {
        this.deviceTime = deviceTime;
    }

    private Date fixTime;

    public Date getFixTime() {
        return fixTime;
    }

    public void setFixTime(Date fixTime) {
        this.fixTime = fixTime;
    }

    public void setTime(Date time) {
        setDeviceTime(time);
        setFixTime(time);
    }

    private boolean outdated;

    @QueryIgnore
    public boolean getOutdated() {
        return outdated;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    private boolean valid;

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double altitude; // value in meters

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    private double speed; // value in knots

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    private double course;

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private double accuracy;

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    private Network network;

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    private String originalMessage;

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    @Override
    @QueryIgnore
    public String getType() {
        return super.getType();
    }

}
