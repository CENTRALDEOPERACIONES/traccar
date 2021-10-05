package org.traccar.notificators;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.Context;
import org.traccar.model.Device;
import org.traccar.model.Event;
import org.traccar.model.Position;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificatorTwilio extends Notificator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificatorTelegram.class);

    private String url;
    private String chatId;

    public static class Message {
        @JsonProperty("uniqueid")
        private String uniqueId;
        @JsonProperty("deviceid")
        private Long deviceId;
        @JsonProperty("serverTime")
        private Date serverTime;
        @JsonProperty("date")
        private String date;
        @JsonProperty("type")
        private String type;
        @JsonProperty("latitude")
        private double latitude;
        @JsonProperty("longitude")
        private double longitude;
    }

    public NotificatorTwilio() {
        url = "https://apiavl.centraldeoperaciones.com/user/SendEmail";
    }

    @Override
    public void sendSync(long userId, Event event, Position position) {

        if(position != null && position.getValid()){
            Message message = new Message();
            Device device = Context.getIdentityManager().getById(event.getDeviceId());
            message.latitude = position.getLatitude();
            message.longitude = position.getLongitude();
            message.uniqueId = device.getUniqueId();
            message.deviceId = event.getDeviceId();
            message.serverTime = event.getEventTime();
            if (event.getType().equals(Event.TYPE_ALARM)){
                message.type = event.getString(Position.KEY_ALARM);
            }else{
                message.type = event.getType();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
            message.date = formatter.format(event.getEventTime());

            Context.getClient().target(url).request()
                    .header("Authorization", "eyJhbGciOiJSUzI1NiIsImtpZCI6IjRlOWRmNWE0ZjI4YWQwMjUwNjRkNjY1NTNiY2I5YjMzOTY4NWVmOTQiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiVXN1YXJpbyIsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9nYS1zZWN1cml0eSIsImF1ZCI6ImdhLXNlY3VyaXR5IiwiYXV0aF90aW1lIjoxNjE5NzM2NDg4LCJ1c2VyX2lkIjoiUU1JN3hyTG01TlpXWjJLckpCa1dlZzRUbTRhMiIsInN1YiI6IlFNSTd4ckxtNU5aV1oyS3JKQmtXZWc0VG00YTIiLCJpYXQiOjE2MTk3MzY0ODgsImV4cCI6MTYxOTc0MDA4OCwiZW1haWwiOiJ1c3VhcmlvLmF2bEBhdmwuY29tIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwaG9uZV9udW1iZXIiOiIrNTI5Nzg0NTY3NTM2IiwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJwaG9uZSI6WyIrNTI5Nzg0NTY3NTM2Il0sImVtYWlsIjpbInVzdWFyaW8uYXZsQGF2bC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.FYZePnNPCg2qe3aTRG-fii0joW9yIZ2-8E8EP_pEkSvS50VUgrXvNPWxMx0t6QdpCfp3pUp-HD5j1dMHaus0AAMTfLfqq9GWJ-1MOlA_mWA6F_u8Hrvqd82nMy5BfHLVw4h_d9r17uJ8cKXGt67NmutVqSRcXhBrSFHRsB9uAP2OWC_lkKO2_zfrYjnQs6aPHRP7Rrc87UUxQBlw_OPD6GfC2gT6I_xvhDNW0AeN2q5c7uUPfI6TttbOMV7FY9SFVEt_WKUTMkTbYcgZK_F1-duBfAJGcWCHk5laGhE9Cc0iBgNkre_JuR88LFkBo0wTpGMZUsXB_OOWO_TK-xktbA")
                    .async().post(Entity.json(message), new InvocationCallback<Object>() {
                        @Override
                        public void completed(Object o) {
                        }

                        @Override
                        public void failed(Throwable throwable) {
                            LOGGER.warn("Twilio API error", throwable);
                        }
                    });
        }
    }

    @Override
    public void sendAsync(long userId, Event event, Position position) {
        sendSync(userId, event, position);
    }

}
