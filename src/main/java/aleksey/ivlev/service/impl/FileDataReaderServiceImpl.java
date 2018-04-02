package aleksey.ivlev.service.impl;

import aleksey.ivlev.helpers.InputLineValidator;
import aleksey.ivlev.model.Order;
import aleksey.ivlev.model.OrderType;
import aleksey.ivlev.service.CommandExecutorService;
import aleksey.ivlev.service.DataReaderService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class FileDataReaderServiceImpl implements DataReaderService {

    private static final String LINE_SPLIT_REGEXP = ",";

    private CommandExecutorService commandExecutorService;
    private InputLineValidator validator;
    private File file;

    public FileDataReaderServiceImpl(CommandExecutorService commandExecutorService, File file, InputLineValidator validator) {

        this.commandExecutorService = commandExecutorService;
        this.file = file;
        this.validator = validator;
    }

    @Override
    public void readData() {
        try {
            Files.lines(file.toPath())
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .forEach(line -> {
                        if (line.startsWith("o")) {
                            placeOrder(line);
                        }
                        if (line.startsWith("q")) {
                            executeQuery(line);
                        }
                        if (line.startsWith("c")) {
                            cancelOrder(line);
                        }
                    });

        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }

    private void placeOrder(String line) {
        String[] splittedLine = line.split(LINE_SPLIT_REGEXP);

        if (validator.validateOrderValues(splittedLine)) {
            Integer id = Integer.valueOf(splittedLine[1]);
            OrderType orderType = OrderType.valueOf(splittedLine[2].toUpperCase());
            Integer price = Integer.valueOf(splittedLine[3]);
            Integer size = Integer.valueOf(splittedLine[4]);

            Order order = new Order(id, orderType, price, size);
            commandExecutorService.placeOrder(order);
        } else {
            System.exit(1);
        }
    }

    private void executeQuery(String line) {
        String[] splittedLine = line.split(LINE_SPLIT_REGEXP);
        if (validator.validateQueryValues(splittedLine)) {
            if (splittedLine.length == 3) {
                Integer result = commandExecutorService.executeTotalQuery(Integer.valueOf(splittedLine[2]));
                System.out.println(result == 0 ? "" : result);
            } else {
                Optional<OrderType> orderType = Optional.ofNullable(OrderType.fromValue(splittedLine[1]));
                orderType.ifPresent(ot -> System.out.println(commandExecutorService.executeQuery(ot)));
            }
        } else {
            System.exit(1);
        }


    }

    private void cancelOrder(String line) {
        String[] splittedLine = line.split(LINE_SPLIT_REGEXP);
        if (validator.validateCancelValue(splittedLine)) {
            commandExecutorService.cancelOrder(Integer.valueOf(splittedLine[1]));
        } else {
            System.exit(1);
        }

    }
}
