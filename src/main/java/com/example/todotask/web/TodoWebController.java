package com.example.todotask.web;

import com.example.todotask.domain.Task;
import com.example.todotask.form.TaskForm;
import com.example.todotask.service.TodoService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class TodoWebController {

    private static final String TASKS = "tasks";

    private static final String REDIRECT_TO = "redirect:/" + TASKS;

    private TodoService todoService;

    @Autowired
    public TodoWebController(TodoService todoService){
        this.todoService = todoService;
    }

    // タスクの全件検索
    @GetMapping(value = "/tasks")
    public ModelAndView readAllTasks(){

        TaskForm form = new TaskForm("", LocalDate.now(), true, true);

        ModelAndView modelAndView = new ModelAndView(TASKS);
        modelAndView.addObject("form", form);

        List<Task> tasks = todoService.findAllTask();
        modelAndView.addObject(TASKS, tasks);

        return modelAndView;
    }

    // タスクを１件作成
    @PostMapping(value = "/tasks")
    public ModelAndView createOneTask(@ModelAttribute TaskForm taskForm){

        Task task = new Task(taskForm.getSubject(), taskForm.getDeadline(), taskForm.getHasDone());
        todoService.createTask(task);
        return new ModelAndView(REDIRECT_TO);
    }

    // タスクを１件取得
    @GetMapping(value = "/tasks/{id}")
    public ModelAndView readOneTask(@PathVariable Integer id){

        Optional<TaskForm> form = readTaskFromId(id);

        if (!form.isPresent()){
            return new ModelAndView(REDIRECT_TO);
        }

        ModelAndView modelAndView = new ModelAndView(TASKS);
        modelAndView.addObject("taskId", id);
        modelAndView.addObject("form", form.get());

        List<Task> tasks = todoService.findAllTask();
        modelAndView.addObject(TASKS, tasks);

        return modelAndView;
    }

    private Optional<TaskForm> readTaskFromId(Integer id){

        Optional<Task> task = todoService.findOneTask(id);
        if(!task.isPresent()){
            return Optional.empty();
        }

        TaskForm form = new TaskForm(task.get().getSubject(), task.get().getDeadLine(),
                task.get().getHasDone(), false);
        return Optional.of(form);
    }

    // タスクを１件更新
    @PutMapping(value = "/tasks/{id}")
    public ModelAndView updateOneTask(@PathVariable Integer id, TaskForm taskForm){
        Task task = new Task(taskForm.getSubject(), taskForm.getDeadline(), taskForm.getHasDone());
        todoService.updateTask(task);

        return new ModelAndView(REDIRECT_TO);
    }

    // タスクを１件削除
    @DeleteMapping(value = "/tasks/{id}")
    public ModelAndView deleteOneTask(@PathVariable Integer id){

        Optional<Task> task = todoService.findOneTask(id);
        if (task.isPresent()){
            todoService.deleteTask(id);
        }

        return new ModelAndView(REDIRECT_TO);
    }

}
