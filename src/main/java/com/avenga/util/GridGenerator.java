package com.avenga.util;

import com.avenga.model.CellState;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GridGenerator {

    public static int[][] generateGrid(int length) {
        Random random;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int[][] grid = new int[length][length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = random.nextInt(2);
            }
        }
        return grid;
    }

    public static CellState[][] generateCellStateGrid(int length) {
        Random random;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        CellState[][] grid = new CellState[length][length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = random.nextInt(2) == 1 ? CellState.ALIVE : CellState.DEAD;
            }
        }
        return grid;
    }

    public static int[][] generateGridFromResource(String name) {
        URL resourceUrl = GridGenerator.class.getResource(name);
        try {
            URI uri = resourceUrl.toURI();
            registerJarFileSystem(uri);
            return Files.lines(Paths.get(uri))
                    .map(line -> Arrays.stream(line.split(" "))
                            .mapToInt(Integer::valueOf)
                            .toArray())
                    .toArray(int[][]::new);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] generateGrid1DFromResource(String name) {
        URL resourceUrl = GridGenerator.class.getResource(name);
        try {
            URI uri = resourceUrl.toURI();
            registerJarFileSystem(uri);
            return Files.lines(Paths.get(uri))
                    .map(line -> Arrays.stream(line.split(" "))
                            .mapToInt(Integer::valueOf)
                            .toArray())
                    .flatMapToInt(Arrays::stream)
                    .toArray();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static CellState[][] generateCellStateGridFromResource(String name) {
        URL resourceUrl = GridGenerator.class.getResource(name);
        try {
            URI uri = resourceUrl.toURI();
            registerJarFileSystem(uri);
            return Files.lines(Paths.get(uri))
                    .map(line -> Arrays.stream(line.split(" "))
                            .map(value -> {
                                int number = Integer.parseInt(value);
                                return number == 1 ? CellState.ALIVE : CellState.DEAD;
                            })
                            .toArray(CellState[]::new))
                    .toArray(CellState[][]::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerJarFileSystem(URI uri) throws IOException {
        if (uri.getScheme().equals("jar")) {
            FileSystems.newFileSystem(uri, Collections.emptyMap());
        }
    }
}
