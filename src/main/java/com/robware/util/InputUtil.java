package com.robware.util;

import java.util.Scanner;

public class InputUtil {

    private static Scanner s;

    private static Scanner getScanner() {
        if(s == null) {
            s = new Scanner(System.in);
        }
        return s;
    }

    public static String getInput(String msg) {
        System.out.println(msg);
        return getScanner().nextLine();
    }

}
