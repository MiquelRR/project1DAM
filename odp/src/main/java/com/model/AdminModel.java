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

    private List<Rank> ranks;
    private List<TaskSkill> taskTypes;
    private List<Section> sections;
    private List<Type> types;
    private List<Type> models;
    private List<Type> orders;
    private List<TaskType> liveTasks;
    private Integer lastIdtask;

    private AdminModel() {
        // weekTemplates = Accesdb.readWeekTemplates();
        sections = Accesdb.readAllSections();
        ranks = Accesdb.readRanks();
        staffList = Accesdb.readWorkers();
        taskTypes = Accesdb.readTaskTypes();
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
        LocalDate lastDate = Accesdb.readLastProcessedDate();
        LocalDate today = LocalDate.now();
        LocalDate expected = getExpected(today);

        if (lastDate == null || lastDate.isBefore(expected)) {
            LocalDate sinceDate = (today.isAfter(lastDate)) ? today : lastDate;
            for (Section sec : sections) {
                generateSectionCalendar(sinceDate, expected, sec);
            }
            for (Worker worker : staffList) {
                Accesdb.copySectionCalendarToWorker(worker);
                // generateCalendar(sinceDate, expected, worker);
            }
            lastDate = expected;
            Accesdb.writeLastProcessedDate(expected);
        } else {
            /*
             * for (Section sec : sections) {
             * List<Day> calendar = Accesdb.readCalendarOfSection(sec.getId());
             * sec.setCalendar(calendar);
             * }
             * for (Worker worker : staffList) {
             * List<Day> calendar = Accesdb.readCalendarOf(worker.getIdWorker());
             * worker.setCalendar(calendar);
             * }
             */
        }

    }

    public LocalDate getLastGeneratedDate(){
        return Accesdb.readLastProcessedDate();
    }

    public void modifyAllCalendarWorker(Worker worker, WeekTemplate wt, boolean reduce) {
        List<Day> cal = Accesdb.readCalendarOf(worker.getIdWorker());
        applyWt(wt, cal, reduce);
        Accesdb.removeWorkerCalendar(worker);
        Accesdb.writeCalendar(worker.getIdWorker(), cal);
    }

    private void applyWt(WeekTemplate wt, List<Day> cal, boolean reduce) {
        for (Day day : cal) {
            int time = wt.getWorkingMinutes()[day.getDate().getDayOfWeek().getValue() - 1];
            if (time < day.getWorkTime() || !reduce) {
                day.setWorkTime(time);
            }
        }
    }
    public void modifyWeekCalendarSection(Section sec, WeekTemplate wt, LocalDate monday , boolean reduce){
        LocalDate sunday = monday.plusDays(6);
        List<Integer> secsIds = getSections(sec, wt);
        List<Integer> workersIds = getidWorkersOnSections(secsIds);

        for (Integer secId : secsIds) {
            List<Day> cal = Accesdb.readCalendarOfSection(secId, monday,sunday);
            applyWt(wt, cal, reduce);
            Accesdb.removeSectionCalendar(sec.getId(), monday, sunday);
            Accesdb.writeSectionCalendar(sec.getId(), cal);
        }

        for (Integer idWorker : workersIds) {
            List<Day> cal= Accesdb.readCalendarOf(idWorker, monday, sunday);
            applyWt(wt, cal, reduce);
            Accesdb.removeWorkerCalendar(idWorker, monday, sunday);
            Accesdb.writeCalendar(idWorker, cal);
        }

    }


    public void modifyAllCalendarSection(Section sec, WeekTemplate wt, boolean reduce) {
        List<Integer> secsIds = getSections(sec, wt);
        List<Integer> workersIds = getidWorkersOnSections(secsIds);

        for (Integer secId : secsIds) {
            Accesdb.modifySection(sec);
            List<Day> cal = Accesdb.readCalendarOfSection(secId);
            applyWt(wt, cal, reduce);
            Accesdb.removeSectionCalendar(sec);
            Accesdb.writeSectionCalendar(sec.getId(), cal);
        }

        for (Integer idWorker : workersIds) {
            List<Day> cal = Accesdb.readCalendarOf(idWorker);  
            applyWt(wt, cal, reduce);
            Accesdb.removeWorkerCalendar(idWorker);
            Accesdb.writeCalendar(idWorker, cal);
        }

    }

    public void modifyDayCalendarSection(Section sec, LocalDate date, Integer time, boolean reduce){
        List<Integer> secsIds = new ArrayList<>();
        if(sec==getGeneralSection()){
            for (Section s : sections) {
                secsIds.add(s.getId());                
            }
        } else
        secsIds.add(sec.getId());
        List<Integer> workersIds = getidWorkersOnSections(secsIds);
        for (Integer idWorker : workersIds) {
            Integer tm = Accesdb.getDayWorktime(idWorker,date);
            if(!reduce || tm> time)
            Accesdb.modifyDay(idWorker, date, time);
        }
        for (Integer idSection : secsIds){
            Integer tm =Accesdb.getDaySectionWorktime(idSection, date);
            if(!reduce || tm> time)
            Accesdb.modifySectionDay(idSection, date, time);
            
        }
    }
    
    private List<Integer> getidWorkersOnSections(List<Integer> secsIds){
        List<Integer> workersIds=new ArrayList<>();
        for (Worker worker : staffList) {
            if (secsIds.contains(worker.getSection()))
                workersIds.add(worker.getIdWorker());
        }

        return workersIds;
    }

    private List<Integer>  getSections(Section sec, WeekTemplate wt) {
        List<Integer> secsIds=new ArrayList<>();
        if (sec == getGeneralSection()) {
            for (Section sc : sections) {
                sc.setWeekTemplate(wt);
                secsIds.add(sc.getId());
            }
        } else {
            for (Section sc : sections) {
                if(sc.getId()==sec.getId())
                sc.setWeekTemplate(wt);
            }
            secsIds.add(sec.getId());
        }

        return secsIds;
    }

    public void modifyDayCalendarWorker(Worker worker, LocalDate date, Integer newTimeMins) {
        Accesdb.modifyDay(worker.getIdWorker(), date, newTimeMins);
    }

    public void modifyWeekCalendarWorker(Worker worker, WeekTemplate wt, LocalDate monday, boolean reduce) {
        LocalDate sunday = monday.plusDays(6);
        List<Day> cal = Accesdb.readCalendarOf(worker.getIdWorker(), monday, sunday);
        applyWt(wt, cal, reduce);
        Accesdb.removeWorkerCalendar(worker.getIdWorker(), monday, sunday);
        Accesdb.writeCalendar(worker.getIdWorker(), cal);
    }

    public void writeType(Type type) {
        Accesdb.updateType(type);
    }

    public WeekTemplate getWorkerWeek(Worker worker, LocalDate monday) {
        return Accesdb.getWorkerWeek(worker.getIdWorker(), monday);
    }

    public WeekTemplate getSectionWeek(Section section, LocalDate monday) {
        return Accesdb.getSectionWeek(section.getId(), monday);
    }

    public Section getGeneralSection() {
        for (Section sec : sections) {
            if (sec.getId() == -1)
                return sec;
        }
        return null;
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

    public List<Worker> getAllWorkers() {
        return staffList;
    }

    public List<Worker> getActiveWorkers() {
        List<Worker> returnList = new ArrayList<>();
        for (Worker worker : staffList) {
            if (worker.getActive())
                returnList.add(worker);
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

    private Section getSectionById(Integer id) {
        for (Section sec : sections) {
            if (sec.getId() == id)
                return sec;
        }
        return null;
    }

    private void generateCalendar(LocalDate sinceDate, LocalDate toDate, Worker worker) {
        List<Day> calendar = new ArrayList<>();
        Section sec = getGeneralSection();
        if (worker.getSection() != null) {
            sec = getSectionById(worker.getSection());
        }
        WeekTemplate wt = sec.getWeekTemplate();
        for (LocalDate date = sinceDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            Day day = new Day(date, wt.getTotalTime(date.getDayOfWeek().getValue() - 1));
            calendar.add(day);
        }
        // worker.setCalendar(calendar);
        Accesdb.writeCalendar(worker.getIdWorker(), calendar);
    }

    private void generateCalendar(LocalDate sinceDate, LocalDate toDate, Worker worker, WeekTemplate wt) {
        List<Day> calendar = new ArrayList<>();
        for (LocalDate date = sinceDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            Day day = new Day(date, wt.getTotalTime(date.getDayOfWeek().getValue() - 1));
            calendar.add(day);
        }
        // worker.setCalendar(calendar);
        Accesdb.writeCalendar(worker.getIdWorker(), calendar);
    }

    private void generateSectionCalendar(LocalDate sinceDate, LocalDate toDate, Section sec) {
        List<Day> calendar = new ArrayList<>();

        WeekTemplate wt = sec.getWeekTemplate();
        for (LocalDate date = sinceDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            System.out.println(date + " -> " + wt.getTotalTime(date.getDayOfWeek().getValue() - 1));
            Day day = new Day(date, wt.getTotalTime(date.getDayOfWeek().getValue() - 1));
            calendar.add(day);
        }
        // sec.setCalendar(calendar);
        Accesdb.writeSectionCalendar(sec.getId(), calendar);
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
        // this shoud aply the dates and workes for the tasks without dependencies
        // HERE I'M

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

    public void addSection(String newSectionName) {
        Section newSect = new Section(getNextSection(), newSectionName);
        newSect.setWeekTemplate(getGeneralSection().getWeekTemplate());
        System.out.println(getGeneralSection().getWeekTemplate());
        sections.add(newSect);
        Accesdb.addSection(newSect);
        generateSectionCalendar(LocalDate.now(), Accesdb.readLastProcessedDate(), newSect);
    }

    public void addNewWorker(Worker worker) {
        worker.setRol("WORKER");
        worker.setIdWorker(staffList.size());
        staffList.add(worker);
        Accesdb.addWorker(worker);
        Accesdb.copySectionCalendarToWorker(worker);
        // generateCalendar(LocalDate.now(), Accesdb.readLastProcessedDate(), worker);

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
        if (sec.getId() < 0)
            return false;
        for (Worker worker : staffList) {
            if (worker.getSection() != null && worker.getSection() == sec.getId())
                return false;
        }
        sections.remove(sec);
        Accesdb.removeSection(sec);
        return true;
    }

    public boolean removeRank(Rank rank) {
        for (Worker worker : staffList) {
            if (worker.getRank() != null && worker.getRank() == rank.getId())
                return false;
        }
        ranks.remove(rank);
        Accesdb.removeRank(rank);
        return true;
    }

    public boolean removeTaskType(TaskSkill task) {
        Boolean clearTask = true;
        for (Worker worker : staffList) {
            if (!worker.getAbilities().isEmpty() && worker.getAbilities().contains(task)) {
                clearTask = false;
                break; // Patxi!!
            }
        }
        clearTask = clearTask && !Accesdb.isTaskinUse(task); // IMPROVE: CHECK AT MEMORY, NOT DB.
        if (clearTask) {
            taskTypes.remove(task);
            Accesdb.removeTask(task);
        }
        return clearTask;
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
