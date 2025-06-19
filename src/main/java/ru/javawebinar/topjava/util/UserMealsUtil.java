package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles

        Map<LocalDate, Integer> map1 = new HashMap<>();
        int total = 0;
        for (int i = 0; i < meals.size(); i++) {
            if (map1.containsKey(meals.get(i).getDateTime().toLocalDate())) {
                int sum = map1.get(meals.get(i).getDateTime().toLocalDate());
                sum += meals.get(i).getCalories();
                map1.put(meals.get(i).getDateTime().toLocalDate(), sum);
            } else {
                map1.put(meals.get(i).getDateTime().toLocalDate(), meals.get(i).getCalories());
            }
        }

        Map<LocalDate, Boolean> map2 = new HashMap<>();
        for (LocalDate date : map1.keySet()) {
            map2.put(date, map1.get(date) > caloriesPerDay);
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (int i = 0; i < meals.size(); i++) {
            if (TimeUtil.isBetweenHalfOpen(meals.get(i).getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(
                        new UserMealWithExcess(
                                meals.get(i).getDateTime(),
                                meals.get(i).getDescription(),
                                meals.get(i).getCalories(),
                                map2.get(meals.get(i).getDateTime().toLocalDate())));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDate, Integer> map1 = new HashMap<>();
        meals.forEach(userMeal -> {
                            if (map1.containsKey(userMeal.getDateTime().toLocalDate())) {
                                int sum = map1.get(userMeal.getDateTime().toLocalDate());
                                sum += userMeal.getCalories();
                                map1.put(userMeal.getDateTime().toLocalDate(), sum);
                            } else {
                                map1.put(userMeal.getDateTime().toLocalDate(), userMeal.getCalories());
                            }
                        }
                );
        Map<LocalDate, Boolean> map2 = new HashMap<>();
        map1.forEach((date, count) -> map2.put(date, count > caloriesPerDay));

        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(userMeal -> {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        map2.get(userMeal.getDateTime().toLocalDate()))
                );
            }
        });

        return result;
    }
}
