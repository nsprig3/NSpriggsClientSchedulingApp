package helper;
/**
 * @author Natasha Spriggs
 */

import java.sql.Timestamp;
import java.time.*;

/**
 * This class assists with time conversions between user time, UTC and EST.
 */

public class timeZone {
    public static ZoneId utcZoneId = ZoneId.of("UTC");
    public static ZoneId userZoneId = ZoneId.systemDefault();
    public static ZoneId estZoneId = ZoneId.of("America/New_York");
    public static LocalTime businessStartEST = LocalTime.of(8,0);
    public static LocalTime businessEndEST = LocalTime.of(20,0);
    public static LocalDateTime userDateTime = LocalDateTime.now(userZoneId);
    public static ZonedDateTime estZonedDateTime = ZonedDateTime.of(userDateTime, utcZoneId);

    /**
     * This method receives a LocalDateTime and converts it to EST to compare the time to the business hours.
     * @param ldt the LocalDateTime that user is trying to use to schedule an appointment.
     * @return localtoEst, the date/time that has been converted to EST.
     */

    public static LocalDateTime toEST (LocalDateTime ldt) {
        //zdt of user
        ZonedDateTime localZDT  = ZonedDateTime.of(ldt, userZoneId);
        ZonedDateTime estZDT = localZDT.withZoneSameInstant(estZoneId);
        LocalDateTime localtoEst = estZDT.toLocalDateTime();
        return localtoEst;
    }

    /**
     * This method receives a date, an appointment start date and time, and an appointment end date and time to determine
     * if the times are outside business hours.
     * @param startDate LocalDate, the date the appointment is scheduled, used to create a LocalDateTime in EST.
     * @param appointmentStart LocalDateTime, the start time for the appointment.
     * @param appointmentEnd LocalDateTIme, the end time for the appointment.
     * @return false if the appointment starts before business hours or ends after business hours; othewise, returns true;
     */

    public static boolean isValidTime (LocalDate startDate, LocalDateTime appointmentStart, LocalDateTime appointmentEnd){
        LocalDateTime businessStart = LocalDateTime.of(startDate, businessStartEST);
        LocalDateTime businessEnd = LocalDateTime.of(startDate, businessEndEST);

        if(appointmentStart.isBefore(businessStart)){
            return false;
        }
        else if(appointmentEnd.isAfter(businessEnd)){
            return false;
        }
        return true;
    }

}
