package aleksey.ivlev.helpers;

import aleksey.ivlev.model.OrderType;

import java.util.Arrays;

import static java.util.Objects.nonNull;

public class InputLineValidator {

    private static final String INT_REGEXP = "\\d+";

    public boolean validateOrderValues(String[] values) {
        if (values.length != 5) {
            System.out.println(String.format("Wrong numbers of input params for order : %s", Arrays.toString(values)));
            return false;
        }

        if (!(values[0].equals("o") && values[1].matches(INT_REGEXP) &&
                nonNull(OrderType.fromName(values[2])) &&
                values[3].matches(INT_REGEXP) &&
                values[4].matches(INT_REGEXP))) {
            System.out.println(String.format("Wrong input params for order : %s", Arrays.toString(values)));
            return false;
        }
        return true;

    }

    public boolean validateQueryValues(String[] values) {
        if (values.length == 2) {
            if (!(values[0].equals("q") && nonNull(OrderType.fromValue(values[1])))) {
                System.out.println(String.format("Wrong input params for query : %s", Arrays.toString(values)));
                return false;
            }
            return true;
        }
        if (values.length == 3) {
            if (!(values[0].equals("q") && values[1].equals("size") && values[2].matches(INT_REGEXP))) {
                System.out.println(String.format("Wrong input params for query : %s", Arrays.toString(values)));
                return false;
            }
            return true;
        }
        System.out.println(String.format("Wrong numbers of input params for query : %s", Arrays.toString(values)));
        return false;

    }

    public boolean validateCancelValue(String[] values) {
        if (values.length != 2) {
            System.out.println(String.format("Wrong numbers of input params for query : %s", Arrays.toString(values)));
            return false;
        }

        if (!(values[0].equals("c") && values[1].matches(INT_REGEXP))) {
            System.out.println(String.format("Wrong input params for cancel order : %s", Arrays.toString(values)));
            return false;
        }

        return true;

    }

}
