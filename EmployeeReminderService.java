package com.javatechie.async;





import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

//import com.completablefuture.Employee;
//import com.completablefuture.EmployeeDatabase;
//import com.completablefuture.EmployeeReminderService;



public class EmployeeReminderService {

    public CompletableFuture<Void> sendReminderToEmployee() {

        Executor executor = Executors.newFixedThreadPool(5);

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("fetchEmployee : " + Thread.currentThread().getName());
            return EmployeeDatabase.fetchEmployees();
        }, executor)
        .thenApplyAsync(employees -> filterNewJoinerEmployees(employees), executor)
        .thenApplyAsync(employees -> filterTrainingNotCompleteEmployees(employees), executor)
        .thenApplyAsync(employees -> getEmails(employees), executor)
        .thenAcceptAsync(emails -> sendEmails(emails), executor);

        return voidCompletableFuture;
    }

    public List<Employee> filterNewJoinerEmployees(List<Employee> employees) {
        System.out.println("filter new joiner employee  : " + Thread.currentThread().getName());
        return employees.stream()
                .filter(employee -> "TRUE".equals(employee.getNewJoiner()))
                .collect(Collectors.toList());
    }

    public List<Employee> filterTrainingNotCompleteEmployees(List<Employee> employees) {
        System.out.println("filter training not complete employee  : " + Thread.currentThread().getName());
        return employees.stream()
                .filter(employee -> "TRUE".equals(employee.getlearningPending()))
                .collect(Collectors.toList());
    }

    public List<String> getEmails(List<Employee> employees) {
        System.out.println("get emails  : " + Thread.currentThread().getName());
        return employees.stream().map(Employee::getEmail).collect(Collectors.toList());
    }

    public void sendEmails(List<String> emails) {
        System.out.println("send email  : " + Thread.currentThread().getName());
        emails.forEach(EmployeeReminderService::sendEmail);
    }

    public static void sendEmail(String email) {
        System.out.println("sending training reminder email to : " + email);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EmployeeReminderService service = new EmployeeReminderService();
        service.sendReminderToEmployee().get();
    
}
}
                
