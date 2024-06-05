package com.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//-------------------------------- Singleton 
public class AdminModel {
    private static AdminModel adminModel; // ---- Singleton
    private List<Worker> staffList;
    private List<Worker> admins;
    // private List<Worker> filteredStaff = new ArrayList<>();

    private Map<Integer, WeekTemplate> weekTemplates;
    private List<Rank> ranks;
    private List<TaskSkill> taskTypes;
    private List<Section> sections;
    private List<Type> types;
    private List<Type> models;
    private List<Type> orders;
    private List<TaskType> liveTasks;
    private Integer lastIdtask;

    private AdminModel() {
        weekTemplates = Accesdb.readWeekTemplates();
        staffList = Accesdb.readWorkers();
        ranks = Accesdb.readRanks();
        taskTypes = Accesdb.readTaskTypes();
        sections = Accesdb.readAllSections();
        types = Accesdb.readAllTypes();
        models = Accesdb.readAllModels();
        orders = Accesdb.readAllOrders();
        Integer lt = Accesdb.getLastLiveIdTask();
        lastIdtask = (lt == null) ? 0 : lt;
        liveTasks = Accesdb.readLiveTasks();

        Map<Integer, List<TaskType>> toAttachInTypes = Accesdb.readLivetaskModels();

        for (Type t : types) {
            List<TaskType> listToAtach = toAttachInTypes.getOrDefault(t.getIdType(), new ArrayList<>());
            t.setTaskList(listToAtach);
        }

        for (Type m : models) {
            List<TaskType> listToAtach = toAttachInTypes.getOrDefault(m.getIdType(), new ArrayList<>());
            m.setTaskList(listToAtach);
        }
        for (Integer i : toAttachInTypes.keySet()) {

        }

        LocalDate lastDate = Accesdb.readLastProcessedDate();
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
        admins = new ArrayList<>();
        for (Worker worker : staffList) {
            List<Integer> list = Accesdb.readTaskTypeIndexesOf(worker.idWorker);
            List<TaskSkill> abilities = new ArrayList<>();
            for (Integer key : list) {
                abilities.add(taskTypes.get(key));
            }
            worker.setAbilities(abilities);
            if (worker.getRol().equals("ADMIN"))
                admins.add(worker);
            if (worker.getRol().equals("ROOT"))
                admins.add(worker);
        }
        staffList.removeAll(admins);

    }

    public void writeType(Type type) {
        Accesdb.updateType(type);
    }

    public int getNextTypeIdx() {
        return types.size() + models.size();
    }

    public List<Type> getTypes() {
        return types;
    }

    public List<Type> getModels() {
        return models;
    }

    public Type getModelById(Integer id) {
        for (Type t : models) {
            if (t.getIdType() == id)
                return t;
        }
        return null;
    }

    public Type getTypeById(Integer id) {
        for (Type t : types) {
            if (t.getIdType() == id)
                return t;
        }
        return null;
    }

    public List<Worker> getAllWorkers(){
        return staffList;
    }

    public List<Worker> getActiveWorkers(){
        List<Worker> returnList = new ArrayList<>();
        for (Worker worker : staffList) {
            if(worker.getActive()) returnList.add(worker);
        }
        return returnList;
    }
    public List<Type> getModelsOf(Type type) {
        List<Type> list = new ArrayList<>();
        if (type != null) {
            for (Type t : this.models) {
                if (t.getModelOf() == type.getIdType())
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

        WeekTemplate wt = weekTemplates.getOrDefault(key, weekTemplates.get(10001));
        for (LocalDate date = sinceDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            Day day = new Day(date, wt.getTotalTime(date.getDayOfWeek().getValue() - 1));
            calendar.add(day);
        }
        worker.setCalendar(calendar);
        Accesdb.writeCalendar(worker.getIdWorker(), calendar);
    }

    public void plan(List<TaskType> list) {
        for (TaskType t : list) {
            if (t.getDependsOn().isEmpty()) {
                Worker wk = null;
                boolean found = false;
                LocalDate today = LocalDate.now();
                LocalDate date = today;
                while (!found) {
                    date.plusDays(1);
                    // here
                    TaskSkill skill = taskTypes.get(t.getTaskRef());
                    List<Worker> wl = getAvaliableWorkersFor(skill, today, t.getPieceTime() + t.getPrepTime());
                    Double unnoc = 0D;
                    Integer[] mins;
                    if (!wl.isEmpty()) {
                        for (Worker w : wl) {
                            mins = dayAvaliability(w, date);
                            if (mins[0] != 0) {
                                double result = (double) mins[1] / mins[0];
                                if (result > unnoc) {
                                    unnoc = result;
                                    wk = w;
                                }
                                if (unnoc > 0) {
                                    found = true;
                                }
                            }
                        }
                    }

                }
                if (wk != null)
                    t.setIdWorker(wk.getIdWorker());
                t.setDate(date);
            }
        }
        //this shoud aply the dates and workes for the tasks without dependencies
        //HERE I'M

    }

    public List<Worker> getAvaliableWorkersFor(TaskSkill skill, LocalDate day, Integer neededMinutes) {
        List<Worker> result = new ArrayList<>();
        for (Worker worker : staffList) {
            if (worker.getAbilities().contains(skill)) {
                Integer[] da = dayAvaliability(worker, day);
                if (da[1] >= neededMinutes)
                    result.add(worker);
            }
        }
        return result;
    }

    /**
     * must return return[0] minutes total, return[1] free minutes
     * 
     * @param worker
     * @param day
     * @return
     */
    public Integer[] dayAvaliability(Worker worker, LocalDate day) {
        Integer workingTime = worker.getCalendar(day);
        Integer restingTime = workingTime;
        for (TaskType t : liveTasks) {
            if (t.getIdWorker() == worker.getIdWorker())
                restingTime -= (t.getPrepTime() + t.getPieceTime());
        }
        return new Integer[] { workingTime, restingTime };
    }

    private LocalDate getExpected(LocalDate today) {
        int nextYear = today.getYear() + 1;
        return YearMonth.of(nextYear, 2).atEndOfMonth();

    }

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
        if (!staffList.isEmpty())
            return staffList.get(staffList.size() - 1);
        else {
            Worker w = new Worker(staffList.size());
            return w;
        }

    }

    public Integer getNextIdTask() {
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
        Accesdb.updateWorker(worker);
    }

    public void updateType(Type type) {
        Accesdb.updateType(type);
    }

    private int getNextSection() {
        int next = 19999;
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
        Accesdb.addTask(task);
    }

    public void addRank(String newRankName) {
        Rank newRank = new Rank(getNextRank(), newRankName);
        ranks.add(newRank);
        Accesdb.addRank(newRank);
    }

    public void addType(String typeName) {
        Type newType = new Type(getNextTypeIdx(), typeName);
        types.add(newType);
        Accesdb.addType(newType);
    }

    // DUPLICATED CODE, this is an case of addOrder with units=1
    public Type addModel(String typeName, Type modelOf) {
        Type newModel = new Type(getNextTypeIdx(), typeName, modelOf.getIdType());
        Bidimap refsMap = new Bidimap();
        for (TaskType tt : modelOf.getTaskList()) {
            Integer newId = getNextIdTask();
            TaskType t2 = new TaskType(newId, getNextTypeIdx(), tt);
            refsMap.put(t2, tt);
        }
        for (TaskType t2 : refsMap.keySet()) {
            List<TaskType> dependsOnNew = new ArrayList<>();
            for (TaskType tt : refsMap.getValue(t2).getDependsOn()) {
                dependsOnNew.add(refsMap.getKeyOf(tt));
            }
            t2.setDependsOn(dependsOnNew);
        }
        newModel.setTaskList(new ArrayList<>(refsMap.keySet()));
        models.add(newModel);
        Accesdb.addType(newModel);
        return newModel;
        // at this moment cloned tasktypes aren't saved to ddbb, will be done later when
        // edited
    }

    public Type addOrder(String orderReference, Type modelOf, Integer units) {
        Type newOrder = new Type(getNextTypeIdx(), orderReference, modelOf.getIdType());
        Bidimap refsMap = new Bidimap();
        for (TaskType tt : modelOf.getTaskList()) {
            Integer newId = getNextIdTask();
            TaskType t2 = new TaskType(newId, getNextTypeIdx(), tt);
            t2.setPieceTime(tt.getPieceTime() * units); // here it is the product
            refsMap.put(t2, tt);
        }
        for (TaskType t2 : refsMap.keySet()) {
            List<TaskType> dependsOnNew = new ArrayList<>();
            for (TaskType tt : refsMap.getValue(t2).getDependsOn()) {
                dependsOnNew.add(refsMap.getKeyOf(tt));
            }
            t2.setDependsOn(dependsOnNew);
        }
        newOrder.setTaskList(new ArrayList<>(refsMap.keySet()));
        orders.add(newOrder);
        Accesdb.addType(newOrder);
        return newOrder;
        // at this moment cloned tasktypes aren't saved to ddbb, will be done later when
        // edited
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

    public TaskSkill getTaskByName(String name) {
        for (TaskSkill tk : taskTypes) {
            if (tk.getName().equals(name))
                return tk;
        }
        return null;
    }

    public String getNameSkillById(Integer id) {
        for (TaskSkill tk : taskTypes) {
            if (tk.getId() == id)
                return tk.getName();
        }
        return null;

    }

    public List<Section> getSections() {
        return sections;
    }

}
