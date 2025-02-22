package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class WialonProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        var decoder = new WialonProtocolDecoder(null);

        verifyNull(decoder, text(
                "#L#2.0;42001300083;;CE45"));

        verifyAttributes(decoder, text(
                "#D#120319;112003;NA;NA;NA;NA;0.000;NA;NA;0;NA;NA;NA;NA;NA;101_521347:1:521246,101_158:1:510,101_521055:1:510,101_521055_2.9:1:509,101_521056:1:3;626B"));

        verifyNull(decoder, text(
                "#L#123456789012345;test"));
        
        verifyNull(decoder, text(
                "#L#2002;NA"));
        
        verifyNull(decoder, text(
                "#P#"));

        verifyPosition(decoder, text(
                "#D#101118;061143;0756.0930;N;12338.6403;E;18.223;99.766;-4.000;10;0.800;NA;NA;NA;NA;101_521347:1:521249,101_521126:1:6593598,101_521127:1:774780,101_521072_21.1:1:0,101_521072_21.2:1:71353;F24A"));

        verifyPosition(decoder, text(
                "99999999#D#101118;061143;0756.0930;N;12338.6403;E;18.223;99.766;-4.000;10;0.800;NA;NA;NA;NA;101_521347:1:521249,101_521126:1:6593598,101_521127:1:774780,101_521072_21.1:1:0,101_521072_21.2:1:71353;F24A"));

        verifyPosition(decoder, text(
                "#D#151216;135910;5321.1466;N;04441.7929;E;87;156;265.000000;12;1.000000;241;NA;NA;NA;odo:2:0.000000,total_fuel:1:430087,can_fls:1:201,can_taho:1:11623,can_mileage:1:140367515"));

        verifyPosition(decoder, text(
                "#D#151216;140203;5312.59514;N;04830.37834;E;53;273;NA;10;NA;NA;NA;NA;NA;EvId:1:1,Board:2:12.81,Accum:2:4.28"));

        verifyPosition(decoder, text(
                "#SD#270413;205601;5544.6025;N;03739.6834;E;1;2;3;4"),
                position("2013-04-27 20:56:01.000", true, 55.74338, 37.66139));

        verifyPosition(decoder, text(
                "99999999#SD#270413;205601;5544.6025;N;03739.6834;E;1;2;3;4"),
                position("2013-04-27 20:56:01.000", true, 55.74338, 37.66139));

        verifyPosition(decoder, text(
                "#SD#021214;065947;2237.7552;N;11404.8851;E;0.000;;170.9;5"));

        verifyPosition(decoder, text(
                "#D#270413;205601;5544.6025;N;03739.6834;E;1;2;3;4;0.0;0;0;14.77,0.02,3.6;NA;count1:1:564,fuel:2:45.8,hw:3:V4.5"));
        
        verifyPosition(decoder, text(
                "#D#190114;051312;4459.6956;N;04105.9930;E;35;306;204.000000;12;NA;452986639;NA;106.000000;NA;sats_gps:1:9,sats_glonass:1:3,balance:2:12123.000000,stay_balance:1:0"));
        
        verifyPosition(decoder, text(
                "#D#021214;065947;2237.7552;N;11404.8851;E;0.000;;170.9;5;1.74;NA;NA;NA;NA;NA"));

        verifyPosition(decoder, text(
                "#D#021214;065947;2237.7552;N;11404.8851;E;0.000;;170.9;5;1.74;NA;NA;;NA;NA"));
    }

}
