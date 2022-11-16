package com.jwade.ui;

import java.math.BigDecimal;
import java.util.Scanner;

public class UserIOImpl implements UserIO {

    private Scanner sc;

    public UserIOImpl() {
        sc = new Scanner(System.in);
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        int num = 0;
        boolean continues = true;
        do {
            System.out.println(prompt);
            try {
                num = Integer.parseInt(sc.nextLine());
                continues = false;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        } while (continues);
        return  num;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int num;

        do {
            System.out.println(prompt);
            try {
                num = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                num = Integer.MAX_VALUE;
            }
        } while (num < min || num > max);

        return num;
    }

    @Override
    public double readDouble(String prompt) {
        System.out.println(prompt);
        return Double.parseDouble(sc.nextLine());
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double num;

        do {
            System.out.println(prompt);
            try {
                num = Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                num = Double.MAX_VALUE;
            }
        } while (num < min || num > max);

        return num;
    }

    @Override
    public float readFloat(String prompt) {
        System.out.println(prompt);
        return Float.parseFloat(sc.nextLine());
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float num;

        do {
            System.out.println(prompt);
            try {
                num = Float.parseFloat(sc.nextLine());
            } catch (NumberFormatException e) {
                num = Float.MAX_VALUE;
            }
        } while (num < min || num > max);

        return num;
    }

    @Override
    public long readLong(String prompt) {
        System.out.println(prompt);
        return sc.nextLong();
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long num;

        do {
            System.out.println(prompt);
            try {
                num = Long.parseLong(sc.nextLine());
            } catch (NumberFormatException e) {
                num = Long.MAX_VALUE;
            }
        } while (num < min || num > max);

        return num;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        BigDecimal num;
        do {
            System.out.println(prompt);
            try{
                num = new BigDecimal(sc.nextLine());
            } catch (NumberFormatException e){
                num = BigDecimal.valueOf(-1);
            }
        } while (num.doubleValue() < 0);
        return num;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        BigDecimal num;

        do {
            System.out.println(prompt);
            try {
                num = new BigDecimal(sc.nextLine());
            } catch (NumberFormatException e) {
                num = BigDecimal.valueOf(-1);
            }
        } while (num.doubleValue() < min.doubleValue() || num.doubleValue() > max.doubleValue() || num==null);

        return num;
    }
}