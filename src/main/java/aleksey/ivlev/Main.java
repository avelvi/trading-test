package aleksey.ivlev;

import aleksey.ivlev.helpers.InputLineValidator;
import aleksey.ivlev.service.CommandExecutorService;
import aleksey.ivlev.service.DataReaderService;
import aleksey.ivlev.service.impl.CommandExecutorServiceImpl;
import aleksey.ivlev.service.impl.FileDataReaderServiceImpl;
import aleksey.ivlev.model.OrderBook;
import aleksey.ivlev.storage.OrderBookStorage;

import java.io.File;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        validateInputParams(args);

        Optional<File> fileOptional = getFileFromInputParams(args);

        fileOptional.ifPresent(file -> {
            OrderBookStorage orderBookStorage = new OrderBookStorage(OrderBook.getInstance());
            CommandExecutorService commandExecutorService = new CommandExecutorServiceImpl(orderBookStorage);
            InputLineValidator validator = new InputLineValidator();
            DataReaderService dataReaderService = new FileDataReaderServiceImpl(commandExecutorService, file, validator);
            dataReaderService.readData();
        });

    }

    private static void validateInputParams(String[] args) {

        if (args.length != 1) {
            System.out.println(
                    String.format("Wrong number of args. Required: %s)", "\"path to file\"")
            );
            System.exit(1);
        }

    }

    private static Optional<File> getFileFromInputParams(String[] args) {
        File file = new File(args[0]);
        if (!file.exists() || file.isDirectory()) {
            System.out.println(String.format("File [%s] not found", file.getAbsolutePath()));
            return Optional.empty();
        }
        return Optional.of(file);
    }

}
