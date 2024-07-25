package com.som.streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootApplication
public class JavaStreamsApplication {

    static List<Employee> employees = new ArrayList<>();

    static {
        employees.add(new Employee("Somnath", "Mukherjee", 5000.0, "QE", List.of("Project 1", "Project 2")));
        employees.add(new Employee("Papai", "Mukherjee", 6000.0, "QE", List.of("Project 3", "Project 4")));
        employees.add(new Employee("Shivam", "Gupta", 5500.0, "DEV", List.of("Project 1", "Project 4")));
        employees.add(new Employee("Debmalya", "Pan", 5555.0, "DEVOPS", List.of("Project 2", "Project 3")));
        employees.add(new Employee("Sourav", "Dey", 6600.0, "DEVOPS", List.of("Project 2", "Project 4")));
        employees.add(new Employee("Suvam", "Tewari", 6700.0, "DEVOPS", List.of("Project 3", "Project 4")));
        employees.add(new Employee("Debjit", "Chakraborty", 5000.0, "DEV", List.of("Project 4", "Project 3")));

    }

    public static void main(String[] args) throws NoSuchFieldException {
//		SpringApplication.run(JavaStreamsApplication.class, args);

        //foreach
        employees.stream()
                .forEach(System.out::println);

        //map
        List<Employee> increasedSalary = employees.stream()
                .map(employee -> new Employee(
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getSalary() * 1.10,
                        employee.getDept(),
                        employee.getProjects()
                ))
                .collect(Collectors.toList());
        System.out.println(increasedSalary);

        //filter
        employees.stream()
                .filter(employee -> employee.getSalary() > 5000.0)
                .map(employee -> new Employee(
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getSalary() * 1.10,
                        employee.getDept(),
                        employee.getProjects()
                ))
                .collect(Collectors.toList()).forEach(System.out::println);

        //findFirst
        Optional<Employee> optionalEmployee = employees.stream()
                .filter(employee -> employee.getSalary() > 5000.0)
                .map(employee -> new Employee(
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getSalary() * 1.10,
                        employee.getDept(),
                        employee.getProjects()
                ))
                .findFirst();
        Employee em = optionalEmployee.orElse(null);
        System.out.println("FindFirst - " + em);

        //flatMap
        String projects = employees.stream()
//				.map(employee -> employee.getProjects())
                .map(Employee::getProjects)
                .flatMap(strings -> strings.stream())
                .collect(Collectors.joining(","));
        System.out.println("Projects - " + projects);

        //short-circuiting operations
        employees.stream()
                .skip(1)
                .limit(1)
                .collect(Collectors.toList()).forEach(System.out::println);

        //Finite Data
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

        //sorting
        employees.stream()
                .sorted(Comparator.comparing(Employee::getFirstName))
                .forEach(System.out::println);

        //min or max
        Employee emp = employees.stream()
                .max(Comparator.comparing(Employee::getSalary)).orElseThrow(NoSuchFieldException::new);
        System.out.println("Employee with max salary - " + emp);

        //reduce
        Double totalSalary = employees.stream()
                .map(Employee::getSalary)
                .reduce(0.0, Double::sum);
        System.out.println("Total Salary - " + totalSalary);

        //distinct - (override the equals and hashcode in Employee object)
        employees.stream()
                .distinct().collect(Collectors.toList()).forEach(System.out::println);

        //get the distinct employee objects without using .distinct()
        Set<String> set = new HashSet<>();
        employees.stream()
                .filter(employee -> set.add(employee.getFirstName() + employee.getLastName()))
                .collect(Collectors.toList()).forEach(System.out::println);

        //get the employee with 2nd highest salary
        Optional<Employee> secondHighestSalaryEmployee = employees.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .skip(1)
                .findFirst();
        secondHighestSalaryEmployee.ifPresent(System.out::println);

        //get the employees with 2nd highest salary
        Map.Entry<Double, List<String>> secondHighestSalaryEmployees = employees.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .collect(Collectors.groupingBy(Employee::getSalary,
                        Collectors.mapping(e -> e.getFirstName() + " " + e.getLastName(), Collectors.toList())))
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors.toList())
                .get(3);
        System.out.println("Employees with same salaries - " + secondHighestSalaryEmployees);

        //Employees with highest salaries per dept
        Map<String, Employee> employeesWithHighestSalaryPerDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDept,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)), Optional::get)
                ));

        System.out.println("Employees with highest salary per depts -" + employeesWithHighestSalaryPerDept);
        //count the characters in a given string
        String input = "ilovejavaverymuch";
        Map<String, Long> countChars = Arrays.stream(input.split("")).collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println("Count characters -" + countChars);

        //find all the duplicate characters from a given String
        List<String> duplicateCharacters = Arrays.stream(input.split(""))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("Duplicate Characters - " + duplicateCharacters);

        //find first unique character from a given String
        String firstUniqueChar = Arrays.stream(input.split(""))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1)
                .findFirst().get().getKey();
        System.out.println("First unique character - " + firstUniqueChar);

        //find second-highest number from a given array
        int[] nums = {5, 9, 11, 2, 8, 21, 1};
        int secondHighestNumber = Arrays.stream(nums)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst().get();

        System.out.println("Second highest number is " + secondHighestNumber);

        //find the longest string from an array
        String[] strArray = {"java", "javascript", "python", "springboot", "microservices"};

        String s = Arrays.stream(strArray)
//				.reduce((word1, word2)->word1.length()>word2.length()?word1:word2)
                .max(Comparator.comparing(String::length))
                .get();
        System.out.println("Longest string is " + s);


        //separate odd and even numbers in a list of integers
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Map<Boolean, List<Integer>> separatedNumbers = numbers.stream()
                .collect(Collectors.partitioningBy(num -> num % 2 == 0));
        List<Integer> evenNumbers = separatedNumbers.get(true);
        List<Integer> oddNumbers = separatedNumbers.get(false);

        System.out.println("Odd Numbers - " + evenNumbers);
        System.out.println("Even numbers - " + oddNumbers);

        //find frequency of each character
        String str = "example string with repeated characters";
        Map<Character, Long> frequencyMap = str.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> c != ' ')
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        frequencyMap.forEach((character, freq) ->
                System.out.println(character + " : " + freq));

        //Find the frequency of each element in an array or a list
        int[] array = {1, 2, 2, 3, 3, 4, 4, 4, 4, 3, 3};
        Map<Integer, Long> freqMap = Arrays.stream(array)
                .boxed()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        freqMap.forEach((element, frequency) ->
                System.out.println(element + " : " + frequency));

        //sort a given list of decimals in reverse order
        List<Double> decimals = Arrays.asList(1.2, 3.4, 2.5, 4.1);
        List<Double> sortedDecimals = decimals.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        //join a list of Strings with '[' as prefix, ']' as suffix and ',' as delimiter
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String joinedString = strings.stream()
                .collect(Collectors.joining(",", "[", "]"));
        System.out.println(joinedString);

        //Merge two unsorted arrays into a single sorted array
        int[] arr1 = {5, 2, 8, 1};
        int[] arr2 = {7, 3, 4, 6};

        int[] mergedArray = IntStream.concat(Arrays.stream(arr1), Arrays.stream(arr2))
                .sorted()
                .toArray();
        System.out.println("Merged and sorted array: " + Arrays.toString(mergedArray));

        //print first 10 even numbers
        IntStream.iterate(2, n -> n+2)
                .limit(10)
                .forEach(System.out::println);

        //Get the last element of an array
        int[] intArr = {1,2,3,4,5};
        OptionalInt lastElement = Arrays.stream(intArr)
                .reduce((first, second) -> second);
        System.out.println("Last element "+lastElement.getAsInt());

        //find duplicate character
        String str2 = "programming";
        Map<Character, Long> charCount = str2.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        charCount.entrySet()
                .stream()
                .filter(characterLongEntry -> characterLongEntry.getValue()>1)
                .forEach(characterLongEntry -> System.out.println("Character : "+characterLongEntry.getKey()+", Count: "+characterLongEntry.getValue()));
    }
}
