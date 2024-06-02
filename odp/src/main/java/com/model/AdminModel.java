package com.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.concurrent.Task;

//-------------------------------- Singleton 
public class AdminModel {
    private static AdminModel adminModel; // ---- Singleton
    private List<Worker> staffList;
    private List<Worker> filteredStaff = new ArrayList<>();

    private Map<Integer, WeekTemplate> weekTemplates;
    private List<Rank> ranks;
    private List<TaskSkill> taskTypes;
    private List<Section> sections;
    private List<Type> types;
    private List<Type> models;
    private Integer lastIdtask;

    private AdminModel() {
        lastIdtask=0;
        weekTemplates = Accesdb.readWeekTemplates();
        staffList = Accesdb.readWorkers();
        ranks = Accesdb.readRanks();
        taskTypes = Accesdb.readTaskTypes();
        sections = Accesdb.readAllSections();
        types = Accesdb.readAllTypes();
        lastIdtask= Accesdb.getLastLiveIdTask();
        for (Type type : types) {
            type.setTaskList(Accesdb.readTaskListOfId(type.getIdType()));
            for (TaskType tp : type.getTaskList()) {
                //tp.setDependsOn(new List<Integer>);        TO DO        
            }
        }
        models = Accesdb.readAllModels();
        LocalDate lastDate = Accesdb.readLastProcessedDate();
        System.out.println(lastDate);
        LocalDate today = LocalDate.now();
        LocalDate expected = getExpected(today);

        if (lastDate == null || lastDate.isBefore(expected)) {
            LocalDate sinceDate = (today.isAfter(lastDate)) ? today : lastDate;
            for (Worker worker : staffList) {
                generateCalendar(sinceDate, expected, worker);
            }
            lastDate = expected;
            Accesdb.writeLastProcessedDate(expected);
        } else {
            for (Worker worker : staffList) {
                List<Day> calendar = Accesdb.readCalendarOf(worker.getIdWorker());
                worker.setCalendar(calendar);
            }
        }
        for (Worker worker : staffList) {
            List<Integer> list = Accesdb.readTaskTypeIndexesOf(worker.idWorker);
            List<TaskSkill> abilities = new ArrayList<>();
            for (Integer key : list) {
                abilities.add(taskTypes.get(key));
            }
            worker.setAbilities(abilities);
        }

    }

    private int getNextTypeIdx() {
        return types.size() + models.size();
    }

    public List<Type> getTypes() {
        return types;
    }

    public List<Type> getModels() {
        return models;
    }

    public List<Type> getModelsOf(Type type) {
        List<Type> list = new ArrayList<>();
        if (type != null) {
            for (Type t : this.types) {
                if (type.getModelOf()== t.getIdType())
                    list.add(t);
            }
        }
        return list;
    }

    private void generateCalendar(LocalDate sinceDate, LocalDate toDate, Worker worker) {
        List<Day> calendar = new ArrayList<>();
        Integer key = 1;
        if (weekTemplates.keySet().contains(worker.getIdWorker()))
            key = worker.getIdWorker();
        else if (weekTemplates.keySet().contains(worker.getSection()))
            key = worker.getSection();

        WeekTemplate wt = weekTemplates.getOrDefault(key, weekTemplates.get(1));
        for (LocalDate date = sinceDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            Day day = new Day(date, wt.getTotalTime(date.getDayOfWeek().getValue() - 1));
            calendar.add(day);
        }
        worker.setCalendar(calendar);
        Accesdb.writeCalendar(worker.getIdWorker(), calendar);
    }

    private LocalDate getExpected(LocalDate today) {
        int nextYear = today.getYear() + 1;
        return YearMonth.of(nextYear, 2).atEndOfMonth();

    }

    /*
     * public List<Worker> getFilteredStaff() {
     * return filteredStaff;
     * }
     * 
     * public void filterStaff(Section sec, Rank rank) {
     * filteredStaff.clear();
     * for (Worker worker : staffList) {
     * if ((sec == null || sec.getId() == worker.getSection())
     * && (rank == null || rank.getId() == worker.getRank())
     * && (worker.getRol()!=null && worker.getRol().equals("WORKER")))
     * filteredStaff.add(worker);
     * }
     * }
     */
    private int getNextRank() {
        int next = -1;
        for (Rank rank : ranks) {
            next = (rank.getId() > next) ? rank.getId() : next;
        }
        return ++next;

    }

    public int getNewWorkerIndex() {
        return staffList.size();
    }

    public Section getLastSection() {
        if (!sections.isEmpty())
            return sections.get(sections.size() - 1);
        else
            return null;
    }

    public Rank getLastRank() {
        if (!ranks.isEmpty())
            return ranks.get(ranks.size() - 1);
        else
            return null;
    }

    public Worker getLastWorker() {
        if (staffList.size() > 2)
            return staffList.get(staffList.size() - 1);
        else
            return null;
    }

    public Integer getNextIdTask(){
        return lastIdtask++;

    }

    public Rank getRankById(int id) {
        for (Rank r : ranks) {
            if (r.getId() == id)
                return r;
        }
        return null;
    }

    public Section getSectionById(int id) {
        for (Section s : sections) {
            if (s.getId() == id)
                return s;
        }
        return null;
    }

    public Worker getWorkerById(int id) {
        return staffList.get(id);
    }

    public void addNewWorker(Worker worker) {
        worker.setRol("WORKER");
        worker.setIdWorker(staffList.size());
        staffList.add(worker);
        Accesdb.addWorker(worker);
        generateCalendar(LocalDate.now(), Accesdb.readLastProcessedDate(), worker);

    }

    public int getWorkerStafSize() {
        return staffList.size();
    }

    public void updateWorker(Worker worker) {
        if (worker.getIdWorker() > 1)
            Accesdb.updateWorker(worker);
    }

    public void updateType(Type type){
        Accesdb.updateType(type);
    }

    private int getNextSection() {
        int next = 9999;
        for (Section section : sections) {
            next = (section.getId() > next) ? section.getId() : next;
        }
        return ++next;
    }

    private int getNextTaskType() {
        int next = -1;
        for (TaskSkill task : taskTypes) {
            next = (task.getId() > next) ? task.getId() : next;
        }
        return ++next;
    }

    public boolean removeSection(Section sec) {
        Boolean clearSection = true;
        for (Worker worker : staffList) {
            if (worker.getSection() != null && worker.getSection() == sec.getId()) {
                clearSection = false;
                break; // Patxi!!
            }
            if (clearSection) {
                sections.remove(sec);
                Accesdb.removeSection(sec);
            }
        }
        return clearSection;
    }

    public boolean removeRank(Rank rank) {
        Boolean clearRank = true;
        for (Worker worker : staffList) {
            if (worker.getRank() != null && worker.getRank() == rank.getId()) {
                clearRank = false;
                break; // Patxi!!
            }
            if (clearRank) {
                ranks.remove(rank);
                Accesdb.removeRank(rank);
            }
        }
        return clearRank;
    }

    public boolean removeTaskType(TaskSkill task) {
        Boolean clearTask = true;
        for (Worker worker : staffList) {
            if (!worker.getAbilities().isEmpty() && worker.getAbilities().contains(task)) { 
                clearTask = false;
                break; // Patxi!!
            }
        }
        clearTask = clearTask && !Accesdb.isTaskinUse(task); // IMPROVE: SEE AT MEMORY, NOT DB.
        if (clearTask) {
            taskTypes.remove(task);
            Accesdb.removeTask(task);
        }
        return clearTask;
    }

    public void addSection(String newSectionName) {
        Section newSect = new Section(getNextSection(), newSectionName);
        sections.add(newSect);
        Accesdb.addSection(newSect);
    }

    public void addTask(String newTaskName) {
        TaskSkill task = new TaskSkill(getNextTaskType(), newTaskName);
        taskTypes.add(task);
        System.out.println("<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>"+task.getName());
        Accesdb.addTask(task);
    }

    public void addRank(String newRankName) {
        Rank newRank = new Rank(getNextRank(), newRankName);
        ranks.add(newRank);
        Accesdb.addRank(newRank);
    }

    public void addType(String typeName){
        Type newType = new Type(getNextTypeIdx(), typeName);
        types.add(newType);
        Accesdb.addType(newType);
    }

    public void addType(String typeName, Type modelOf){
        Type newType = new Type(getNextTypeIdx(), typeName, modelOf.getIdType());
        types.add(newType);
        Accesdb.addType(newType);
    }

    public static AdminModel getAdminModel() {
        if (adminModel == null)
            adminModel = new AdminModel();
        return adminModel;
    }

    public void modifyWorkerSkills(Worker worker) {
        Accesdb.modifyWorkerSkills(worker);
    }

    public List<Worker> getStaffList() {
        return staffList;
    }

    public Map<Integer, WeekTemplate> getWeekTemplates() {
        return weekTemplates;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public List<TaskSkill> getTaskTypes() {
        return taskTypes;
    }

    public TaskSkill getTaskByName(String name){
        for (TaskSkill tk : taskTypes) {
            if (tk.getName().equals(name)) 
            return tk;            
        }
        return null;
    }

    public List<Section> getSections() {
        return sections;
    }

}
