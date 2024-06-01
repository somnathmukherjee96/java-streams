package com.som.streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class JavaStreamsApplication {

	static List<Employee> employees = new ArrayList<>();
	static{
		employees.add(new Employee("Somnath", "Mukherjee",5000.0, List.of("Project 1", "Project 2")));
		employees.add(new Employee("Somnath", "Mukherjee",6000.0, List.of("Project 3", "Project 4")));
		employees.add(new Employee("Shivam", "Gupta",5500.0, List.of("Project 1", "Project 4")));
		employees.add(new Employee("Debmalya", "Pan",5555.0, List.of("Project 2", "Project 3")));
		employees.add(new Employee("Sourav", "Dey",6600.0, List.of("Project 2", "Project 4")));
		employees.add(new Employee("Suvam", "Tewari",6700.0, List.of("Project 3", "Project 4")));
		employees.add(new Employee("Debjit", "Chakraborty",8000.0, List.of("Project 4", "Project 3")));

	}
	public static void main(String[] args) throws NoSuchFieldException {
//		SpringApplication.run(JavaStreamsApplication.class, args);

		//foreach
		employees.stream()
				.forEach(employee -> System.out.println(employee));

		//map
		List<Employee> increasedSalary = employees.stream()
				.map(employee -> new Employee(
						employee.getFirstName(),
						employee.getLastName(),
						employee.getSalary() * 1.10,
						employee.getProjects()
				))
				.collect(Collectors.toList());
		System.out.println(increasedSalary);

		//filter
		employees.stream()
				.filter(employee -> employee.getSalary()> 5000.0)
				.map(employee -> new Employee(
						employee.getFirstName(),
						employee.getLastName(),
						employee.getSalary() * 1.10,
						employee.getProjects()
				))
				.collect(Collectors.toList()).forEach(System.out::println);

		//findFirst
		Optional<Employee>optionalEmployee = employees.stream()
				.filter(employee -> employee.getSalary()> 5000.0)
				.map(employee -> new Employee(
						employee.getFirstName(),
						employee.getLastName(),
						employee.getSalary() * 1.10,
						employee.getProjects()
				))
				.findFirst();
		Employee em = optionalEmployee.orElse(null);
		System.out.println("FindFirst - "+em);

		//flatMap
		String projects = employees.stream()
				.map(employee -> employee.getProjects())
				.flatMap(strings -> strings.stream())
				.collect(Collectors.joining(","));
		System.out.println("Projects - "+projects);

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
		System.out.println("Employee with max salary - "+emp);

		//reduce
		Double totalSalary = employees.stream()
				.map(Employee::getSalary)
				.reduce(0.0, Double::sum);
		System.out.println("Total Salary - "+totalSalary);

		//distinct - (override the equals and hashcode in Employee object)
		employees.stream()
				.distinct().collect(Collectors.toList()).forEach(System.out::println);

		//get the distinct employee objects without using .distinct()
		Set<String> set = new HashSet<>();
		employees.stream()
				.filter(employee -> set.add(employee.getFirstName()+employee.getLastName()))
				.collect(Collectors.toList()).forEach(System.out::println);

		//get the employee with 2nd highest salary
		Optional<Employee> secondHighestSalaryEmployee =employees.stream()
				.sorted(Comparator.comparing(Employee::getSalary).reversed())
				.skip(1)
				.findFirst();
        secondHighestSalaryEmployee.ifPresent(System.out::println);
	}

}
