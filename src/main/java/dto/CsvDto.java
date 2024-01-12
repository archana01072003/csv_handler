package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class CsvDto {
    LocalTime vehicleSpeedTime;
    String vehicleSpeed;
    LocalTime engineSpeedTime;
    String engineSpeed;
    LocalTime actualEnginePercentTorqueTime;
    String actualEnginePercentTorque;
    String engineDemandPercentTorque;
    LocalTime transmissionCurrentGearTime;
    String transmissionCurrentGear;
}
