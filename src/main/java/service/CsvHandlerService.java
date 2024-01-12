package service;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import dto.CsvDto;
import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;


public class CsvHandlerService {

    public void test(String filePath) {
        String csvFilePath = "path/to/your/file.csv";  // Replace with the actual path to your CSV file



    }

}





class CSVReaderExample {
    public static void main(String[] args) {
        String csvFilePath = "F:\\test\\speed_torque_trans_gear_03012024.csv";  // Replace with the actual path to your CSV file

        List<String[]> rows = readCSVFile(csvFilePath);

        // Print the contents of the list

        int i = 0;

        for (String[] row : rows) {
            for (String value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();  // Move to the next line for the next row

            if(i++>=10) break;
        }


        List<CsvDto> csvDtoList = convertToDtoList(rows);

        // Sample input LocalTime
//        LocalTime inputTime = LocalTime.parse("14:48:19");

        List<CsvDto> result = new ArrayList<>();

        int resultSize = 0;

        for (CsvDto csvDto : csvDtoList) {
            LocalTime timeStamp = csvDto.getTransmissionCurrentGearTime();

//            // Define a formatter that includes hours, minutes, seconds, and fraction of a second
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSSS");
//
//            // Parse the time stamp
//            LocalTime parsedTime = LocalTime.parse(timeStamp, formatter);
//
//            System.out.println("Parsed Time: " + parsedTime);

            CsvDto nearestDto = findNearestEngineSpeedTimeRow(csvDtoList, timeStamp);

            nearestDto.setTransmissionCurrentGearTime(csvDto.getTransmissionCurrentGearTime());
            nearestDto.setTransmissionCurrentGear(csvDto.getTransmissionCurrentGear());

            result.add(nearestDto);
            System.out.println("Nearest CsvDto: " + nearestDto);

            if(resultSize++ >=10) break;
        }

        String csvOutputFilePath = "F:\\test\\speed_torque_trans_gear_03012024_output.csv";

        // Write the List<CsvDto> to CSV
        writeCsvToFile(result, csvOutputFilePath);

        System.out.println("DONE");
    }

    private static void writeCsvToFile(List<CsvDto> csvDtoList, String csvFilePath) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath))) {
            // Write the header
            String[] header = {"Vehicle Speed Time", "Vehicle Speed", "Engine Speed Time", "Engine Speed",
                    "Actual Engine Percent Torque Time", "Actual Engine Percent Torque", "Engine Demand Percent Torque",
                    "Transmission Current Gear Time", "Transmission Current Gear"};
            csvWriter.writeNext(header);

            // Create a DateTimeFormatter with the desired pattern
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");


            // Write the data
            for (CsvDto dto : csvDtoList) {
                String[] row = {
                        dto.getVehicleSpeedTime()==null?"":dto.getVehicleSpeedTime().format(formatter),
                        dto.getVehicleSpeed(),
                        dto.getEngineSpeedTime().format(formatter),
                        dto.getEngineSpeed(),
                        dto.getActualEnginePercentTorqueTime().format(formatter),
                        dto.getActualEnginePercentTorque(),
                        dto.getEngineDemandPercentTorque(),
                        dto.getVehicleSpeedTime()==null? "" : dto.getTransmissionCurrentGearTime().format(formatter),
                        dto.getTransmissionCurrentGear()
                };
                System.out.println("Row" + row);
                csvWriter.writeNext(row);
            }

            System.out.println("CSV file written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static LocalTime parseDate(String timeStamp) {
//        String timeStamp = "14:48:18:4569";

        if(StringUtils.isEmpty(timeStamp))
            return null;

        // Define a formatter that includes hours, minutes, seconds, and fraction of a second
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSSS");

        // Parse the time stamp
        LocalTime parsedTime = LocalTime.parse(timeStamp, formatter);

        return parsedTime;
    }


    private static CsvDto findNearestEngineSpeedTimeRow(List<CsvDto> csvDtoList, LocalTime inputTime) {
        CsvDto nearestDto = null;
        Duration minDifference =Duration.ofDays(Integer.MAX_VALUE);

        for (CsvDto dto : csvDtoList) {
            Duration difference = Duration.between(dto.getEngineSpeedTime(), inputTime).abs();

            if (difference.compareTo(minDifference) < 0) {
                minDifference = difference;
                nearestDto = dto;
            }
        }

        return nearestDto;
    }

    // Mock method to simulate obtaining List<CsvDto> from CSV rows
//    private static List<CsvDto> getConvertedDtoList() {
//        // Implement logic to obtain List<CsvDto> from CSV rows or other sources
//        // For demonstration purposes, a dummy list is returned here
//        List<CsvDto> dummyDtoList = new ArrayList<>();
//        dummyDtoList.add(new CsvDto(LocalTime.parse("14:48:18"), "100", LocalTime.parse("14:48:19"), "200", LocalTime.parse("14:48:20"), "50", "60", LocalTime.parse("14:48:21"), "3"));
//        // Add more CsvDto objects as needed
//        return dummyDtoList;
//    }
    private static List<CsvDto> convertToDtoList(List<String[]> rows) {
        List<CsvDto> csvDtoList = new ArrayList<>();

        for (String[] row : rows) {
            CsvDto csvDto = new CsvDto();

            // Assuming the CSV columns are in the following order
            csvDto.setVehicleSpeedTime(parseDate(row[0])); // Adjust the index based on your CSV structure
            csvDto.setVehicleSpeed(row[1]);
            csvDto.setEngineSpeedTime(parseDate(row[2]));
            csvDto.setEngineSpeed(row[3]);
            csvDto.setActualEnginePercentTorqueTime(parseDate(row[4]));
            csvDto.setActualEnginePercentTorque(row[5]);
            csvDto.setEngineDemandPercentTorque(row[6]);
            csvDto.setTransmissionCurrentGearTime(parseDate(row[7]));
            csvDto.setTransmissionCurrentGear(row[8]);

            csvDtoList.add(csvDto);
        }

        return csvDtoList;
    }

    // Mock method to simulate reading CSV rows
    private static List<String[]> getCSVRows() {
        // Implement logic to read CSV rows from a file or other source
        // For demonstration purposes, a dummy list is returned here
        List<String[]> dummyRows = new ArrayList<>();
        dummyRows.add(new String[]{"14:48:18", "100", "14:48:19", "200", "14:48:20", "50", "60", "14:48:21", "3"});
        // Add more rows as needed
        return dummyRows;
    }


    private static List<String[]> readCSVFile(String filePath) {
        List<String[]> rows = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                rows.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }
}

