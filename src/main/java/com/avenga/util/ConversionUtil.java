package com.avenga.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversionUtil {

    public static int[] convertToArray(int[][] matrix) {
        int[] result = new int[matrix.length * matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[j + i * matrix.length] = matrix[i][j];
            }
        }
        return result;
    }

    public static int[] convertToArray2(int[][] matrix) {
        return Arrays.stream(matrix)
                .flatMapToInt(Arrays::stream)
                .toArray();
    }

    public static int[][] copy(int[][] matrix) {
        int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, result[i], 0, matrix[i].length);
        }
        return result;
    }
}
