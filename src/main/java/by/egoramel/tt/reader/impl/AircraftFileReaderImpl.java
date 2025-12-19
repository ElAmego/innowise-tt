package by.egoramel.tt.reader.impl;

import by.egoramel.tt.exception.CustomException;
import by.egoramel.tt.reader.AircraftFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class AircraftFileReaderImpl implements AircraftFileReader {
    private final String fileUrl;

    public AircraftFileReaderImpl(final String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public List<String> readAircraftFile() throws CustomException {
        final Path filePath = Path.of(fileUrl);

        try {
            return Files.readAllLines(filePath);
        } catch (final IOException e) {
            throw new CustomException(e);
        }
    }
}