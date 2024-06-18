package com.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Accesdb {

    // DEBUGGING:
    private static boolean local = true;
    private static boolean logMode = true;

    private static LogToFile bbddlog = new LogToFile("queries");
    private final static String BBDD_NAME = "odplanDDBB";
    private final static String bdcon = (local) ? "jdbc:mysql://localhost:3306/" + BBDD_NAME
            : "jdbc:mysql://localhost:33006/" + BBDD_NAME;
    // private final static String bdcon =
    // "jdbc:mysql://localhost:33006/"+BBDD_NAME;
    private final static String us = "root";
    private final static String pw = "root";

    public static void setLogOn() {
        logMode = true;
    }

    public static void setLogOff() {
        logMode = false;
    }

    public static List<Type> readAllTypes() {
        List<Type> list = new ArrayList<>();
        List<String[]> lst = lligTaula("productType");
        for (String[] reg : lst) {
            if (reg[4].equals("TYPE")) {
                Type type = new Type(Integer.parseInt(reg[0]), reg[1], reg[2]);

                list.add(type);
            }
        }
        return list;
    }

    public static List<Type> readAllModels() {
        List<Type> list = new ArrayList<>();
        List<String[]> lst = lligTaula("productType");
        for (String[] reg : lst) {
            if (reg[4].equals("MODEL")) {
                Type type = new Type(Integer.parseInt(reg[0]), reg[1], reg[2], Integer.parseInt(reg[3]));
                list.add(type);
            }
        }
        return list;
    }


    /**
     * in further versions WILL BE IMPROVED to read only live orders
     * 
     * @return
     */
    public static List<Type> readAllOrders() {
        List<Type> list = new ArrayList<>();
        List<String[]> lst = lligTaula("productType");
        for (String[] reg : lst) {
            if (reg[4].equals("ORDER")) {
                Type type = new Type(Integer.parseInt(reg[0]), reg[1], reg[2], Integer.parseInt(reg[3]));
                list.add(type);
            }
        }
        return list;
    }



    public static WeekTemplate getWorkerWeek(Integer id, LocalDate monday){
        return getWeek("idWorker", id, monday);
    }

    public static WeekTemplate getSectionWeek(Integer id, LocalDate monday){
        return getWeek("idSection", id, monday);
    }



    protected static WeekTemplate getWeek(String str,Integer id, LocalDate monday){
        LocalDate saturday=monday.plusDays(5);
        List<String[]> lst =lligQuery("SELECT date, workTime FROM calendar WHERE "+str+" ="+id+" AND date BETWEEN '"+monday+"' AND '"+saturday+"';");
        WeekTemplate wt = new WeekTemplate();
        for (String[] reg : lst) {
            LocalDate date= LocalDate.parse(reg[0]);
            Integer time = Integer.parseInt(reg[1]);
            wt.setTime(date, time);
        }
        return wt;
    }

    

    public static Integer getLastLiveIdTask() {
        String[] n = lligReg("SELECT MAX(idLiveTask) FROM liveTask;");
        if (n == null)
            return 0;
        if (n[0] == null)
            return 0;
        return toInt(n[0]);
    }

    /**
     * Reading DDBB builds dependences of TaskTypes
     * 
     * @return a completed map to attach task with dependences to types or models
     */
    public static Map<Integer, List<TaskType>> readLivetaskModels() {

        Map<Integer, TaskType> taskTypesMap = new HashMap<>();
        Map<Integer, List<TaskType>> map = new HashMap<>();
        List<String[]> lst = lligQuery(
                "SELECT idLiveTask, idproductType, idTask, taskInstructions, initTime, pieceTime FROM " + BBDD_NAME
                        + ".liveTask WHERE date IS null;");
        for (String[] reg : lst) {
            Integer idLliveTask = Integer.parseInt(reg[0]);
            TaskType tt = new TaskType(
                    idLliveTask,
                    Integer.parseInt(reg[1]),
                    Integer.parseInt(reg[2]),
                    skill.get(Integer.parseInt(reg[2])),
                    reg[3],
                    Integer.parseInt(reg[4]),
                    Integer.parseInt(reg[5]));
            taskTypesMap.put(idLliveTask, tt);
        }
        lst = lligQuery("SELECT * FROM " + BBDD_NAME + ".taskDependency;");
        for (String[] reg : lst) {
            Integer taskId = Integer.parseInt(reg[0]);
            Integer depensOnId = Integer.parseInt(reg[1]);
            taskTypesMap.get(taskId).addDependency(taskTypesMap.get(depensOnId));
        }
        for (TaskType tt : taskTypesMap.values()) {
            if (!map.keySet().contains(tt.getTypeRef()))
                map.put(tt.getTypeRef(), new ArrayList<>());
            map.get(tt.getTypeRef()).add(tt);
        }
        return map;
    }

    public static void copySectionCalendarToWorker(Worker w){
        int sectionId=w.getSection();
        int workerId=w.getIdWorker();
        String query="INSERT INTO calendar (idWorker, date, workTime, idSection) SELECT "+workerId+" AS idWorker, date, workTime, NULL AS idSection FROM calendar WHERE idSection = "+sectionId+" AND date >= CURDATE();";
        System.out.println(query);
        modifica(query);
    }

    public static List<TaskType> readLiveTasks() {
        List<TaskType> returnList = new ArrayList<>();
        List<String[]> lst = lligQuery(
                "SELECT idLiveTask, idproductType, idTask, taskInstructions, initTime, pieceTime, date, idWorker FROM " + BBDD_NAME
                        + ".liveTask WHERE date > current_date AND done = 'NO';");
                        for (String[] reg : lst) {
                            Integer idLliveTask = Integer.parseInt(reg[0]);
                            LocalDate date = LocalDate.parse(reg[6]);
                            TaskType tt = new TaskType(
                                    idLliveTask,
                                    Integer.parseInt(reg[1]),
                                    Integer.parseInt(reg[2]),
                                    skill.get(Integer.parseInt(reg[2])),
                                    reg[3],
                                    Integer.parseInt(reg[4]),
                                    Integer.parseInt(reg[5]),
                                    date,
                                    Integer.parseInt(reg[7])
                                    );
                                    returnList.add(idLliveTask, tt);
                        }
        return returnList;
    }

    public static List<Section> readAllSections() {
        List<Section> list = new ArrayList<>();
        List<String[]> lst = lligTaula("section");
        for (String[] reg : lst) {
            Section section = new Section(Integer.parseInt(reg[0]), reg[1]);
            list.add(section);
            WeekTemplate wt =  new WeekTemplate(Integer.parseInt(reg[2]), Integer.parseInt(reg[3]),Integer.parseInt(reg[4]),Integer.parseInt(reg[5]),Integer.parseInt(reg[6]),Integer.parseInt(reg[7]));
            section.setWeekTemplate(wt);
        }
        return list;
    }

    public static void addWorker(Worker worker) { ////
        Object[] ob = new Object[] {
                "idWorker", worker.getIdWorker(),
                "userName", worker.getUserName(),
                "fullName", worker.getFullName(),
                "password", worker.getPasswd(),
                "sinceDate", worker.getSince().toString(),
                "ss", worker.getSsNum(),
                "dni", worker.getDni(),
                "idSection", worker.getSection(),
                "idRank", worker.getRank(),
                "address", worker.getAddress(),
                "telNum", worker.getTelNum(),
                "email", worker.getMail(),
                "contact", worker.getContact(),
                "docFolder", worker.getDocFolder(),
                "active", (worker.getActive()) ? "YES" : "NO",
                "workerRol", "WORKER"
        };

        agrega("worker", ob);

    }

    public static void updateWorker(Worker worker) { ////
        String query = "UPDATE worker SET";
        query += " userName = '" + worker.getUserName();
        query += "', fullName = '" + worker.getFullName();
        query += "', password = '" + worker.getPasswd();
        query += "', sinceDate = '" + worker.getSince().toString();
        query += "', ss = '" + worker.getSsNum();
        query += "', dni = '" + worker.getDni();
        query += "', idSection = " + worker.getSection();
        query += ", idRank = " + worker.getRank();
        query += ", address = '" + worker.getAddress();
        query += "', telNum = '" + worker.getTelNum();
        query += "', email = '" + worker.getMail();
        query += "', contact = '" + worker.getContact();
        query += "', docFolder = '" + worker.getDocFolder();
        query += "', active = " + ((worker.getActive()) ? "'YES'" : "'NO'");
        query += " WHERE idWorker = " + worker.getIdWorker();
        modifica(query);

    }
    public static void addSection(Section sec) {
        WeekTemplate wt=sec.getWeekTemplate();
        agrega(BBDD_NAME + ".section", new Object[] { "idSection", sec.getId(), "name", sec.toString() ,
         "monday",wt.getTotalTime("monday"),
         "tuesday",wt.getTotalTime("tuesday"),
         "wednesday",wt.getTotalTime("wednesday"),
         "thursday",wt.getTotalTime("thursday"),
         "friday",wt.getTotalTime("friday"),
         "saturday",wt.getTotalTime("saturday"),
         "sunday",0
        });
    }

    public static void modifySection(Section sec){
        WeekTemplate wt=sec.getWeekTemplate();
        String query = "UPDATE section SET";
        query += " name = '"+sec.getName();
        query += "', monday = "+wt.getTotalTime("monday");
        query += ", tuesday = "+wt.getTotalTime("tuesday");
        query += ", wednesday = "+wt.getTotalTime("wednesday");
        query += ", thursday = "+wt.getTotalTime("thursday");
        query += ", friday = "+wt.getTotalTime("friday");
        query += ", saturday = "+wt.getTotalTime("saturday");
        query += ", sunday = 0 ";
        query += " WHERE idSection = "+sec.getId()+";";
        modifica(query);
    }


    public static void updateType(Type type) {
        String query = "UPDATE productType SET";
        query += " name = '" + type.getName();
        query += "', mainFolderPath = '" + type.getDocFolder();
        query += "', modelOf = " + type.getModelOf();
        query += ", type = " + ((type.getModelOf() == null) ? "'TYPE'" : "'MODEL'");
        query += " WHERE idproductType = " + type.getIdType();
        modifica(query);
        for (TaskType tk : type.getTaskList()) {
            modifica("DELETE FROM " + BBDD_NAME + ".taskDependency WHERE idtaskInType = " + tk.getId());
        }
        List<Integer[]> bufferDependencies = new ArrayList<>(); // for Miquel on future : you must write on the ddbb
                                                                // dependencies once task are written (Foreing Keys)
        modifica("DELETE FROM " + BBDD_NAME + ".liveTask WHERE idproductType = " + type.getIdType());
        for (TaskType tk : type.getTaskList()) {
            agrega(BBDD_NAME + ".liveTask", new Object[] { "idLiveTask", tk.getId(), "idproductType", type.getIdType(),
                    "idTask", tk.getTaskRef(), "taskInstructions", tk.getInfoFilePath(), "initTime", tk.getPrepTime(),
                    "pieceTime", tk.getPieceTime() });
            for (TaskType td : tk.getDependsOn()) {
                bufferDependencies.add(new Integer[] { tk.getId(), td.getId() });
            }
        }
        for (Integer[] dt : bufferDependencies) {
            agrega(BBDD_NAME + ".taskDependency",
                    new Object[] { "idtaskInType", dt[0], "idtaskInTypeDependeny", dt[1] });
        }

    }

  

    public static void addTask(TaskSkill task) {

        agrega(BBDD_NAME + ".taskType", new Object[] { "idTask", task.getId(), "name", task.toString() });
    }

    public static void addRank(Rank rank) {
        agrega(BBDD_NAME + ".rank", new Object[] { "idRank", rank.getId(), "name", rank.toString() });
    }

    public static void addType(Type type) {
        String typeString = (type.getModelOf() == null) ? "TYPE" : "MODEL";
        agrega(BBDD_NAME + ".productType", new Object[] { "idProductType", type.getIdType(), "name", type.toString(),
                "mainFolderPath", type.getDocFolder(), "modelOf", type.getModelOf(), "type", typeString });
    }

    public static void addOrder(Type type) {
        String typeString = "ORDER";
        agrega(BBDD_NAME + ".productType", new Object[] { "idProductType", type.getIdType(), "name", type.toString(),
                "mainFolderPath", type.getDocFolder(), "modelOf", type.getModelOf(), "type", typeString });
    }

    public static void removeSection(Section sec) {
        modifica("DELETE FROM " + BBDD_NAME + ".calendar WHERE idSection = " + sec.getId());
        modifica("DELETE FROM " + BBDD_NAME + ".section WHERE idSection = " + sec.getId());
    }


    public static void removeRank(Rank rank) {
        modifica("DELETE FROM " + BBDD_NAME + ".rank WHERE idRank = " + rank.getId());
    }

    public static void removeTask(TaskSkill task) {
        modifica("DELETE FROM " + BBDD_NAME + ".taskType WHERE idTask= " + task.getId());
    }

    public static boolean isTaskinUse(TaskSkill task) {
        return lligQuery("SELECT * FROM " + BBDD_NAME + ".liveTask WHERE idTask=" + task.getId()).size() > 0;
    }

    public static LocalDate readLastProcessedDate() {
        LocalDate lastDate = LocalDate.of(1973, 3, 24);
        String gettedDate = lligString("SELECT * FROM lastGeneratedDates ORDER BY lastDate DESC LIMIT 1;");
        if (gettedDate != null) {
            lastDate = LocalDate.parse(gettedDate);
        }
        return lastDate;
    }

    public static List<Rank> readRanks() {
        List<Rank> list = new ArrayList<>();
        List<String[]> lst = lligTaula(BBDD_NAME + ".rank");
        for (String[] reg : lst) {
            Rank rank = new Rank(Integer.parseInt(reg[0]), reg[1]);
            list.add(rank);
        }
        return list;
    }

    public static void writeLastProcessedDate(LocalDate date) {
        agrega("lastGeneratedDates", new Object[] { "lastDate", date.toString() });
    }

    public static void modifyWorkerSkills(Worker worker) {
        String query = "DELETE FROM abilities WHERE idWorker = " + worker.getIdWorker();
        modifica(query);
        for (TaskSkill skill : worker.abilities) {
            agrega("abilities", new Object[] { "idTask", skill.getId(), "idWorker", worker.getIdWorker() });
        }
    }

    public static Map<Integer, WeekTemplate> readWeekTemplates() {
        Map<Integer, WeekTemplate> readedList = new HashMap<>();
        List<String[]> lst = lligTaula("weekTemplate");
        for (String[] reg : lst) {
            int idWeek = Integer.parseInt(reg[0]);
            int idSection = Integer.parseInt(reg[1]);
            int monday = Integer.parseInt(reg[2]);
            int tuesday = Integer.parseInt(reg[3]);
            int wednesday = Integer.parseInt(reg[4]);
            int thursday = Integer.parseInt(reg[5]);
            int friday = Integer.parseInt(reg[6]);
            int saturday = Integer.parseInt(reg[7]);
            WeekTemplate wt = new WeekTemplate(monday, tuesday, wednesday, thursday, friday, saturday);
            readedList.put(idSection, wt);
        }
        return readedList;
    }

    public static List<Integer> readTaskTypeIndexesOf(Integer idWorker) {
        List<Integer> returnList = new ArrayList<>();
        List<String[]> lista = lligQuery("SELECT idTask FROM abilities WHERE idWorker = " + idWorker);
        for (String[] reg : lista) {
            returnList.add(Integer.parseInt(reg[0]));
        }
        return returnList;
    }

    public static List<Day> readCalendarOf(Integer idWorker) {
        List<String[]> list = lligQuery("SELECT date, workTime FROM calendar WHERE date >= CURDATE() AND idWorker=" + idWorker);
        List<Day> returnList = new ArrayList<>();
        for (String[] reg : list) {
            Day day = new Day(LocalDate.parse(reg[0]), Integer.parseInt(reg[1]));
            returnList.add(day);
        }
        return returnList;
    }

    public static List<Day> readCalendarOf(Integer idWorker, LocalDate since, LocalDate toDate) {
        List<String[]> list = lligQuery("SELECT date, workTime FROM calendar WHERE idWorker=" + idWorker+" AND date BETWEEN '"+since+"' AND '"+toDate+"';");
        List<Day> returnList = new ArrayList<>();
        for (String[] reg : list) {
            Day day = new Day(LocalDate.parse(reg[0]), Integer.parseInt(reg[1]));
            returnList.add(day);
        }
        return returnList;
    }

    public static void removeWorkerCalendar(Integer idWorker, LocalDate since, LocalDate toDate) {
        String query= "DELETE FROM calendar WHERE idWorker=" + idWorker+" AND date BETWEEN '"+since+"' AND '"+toDate+"';";
        modifica(query);
    }
    public static void removeWorkerCalendar(Worker worker){
        modifica("DELETE FROM calendar WHERE idWorker="+worker.getIdWorker());
    }
    public static void removeWorkerCalendar(Integer idWorker){
        modifica("DELETE FROM calendar WHERE idWorker="+idWorker);
    }

    public static void removeSectionCalendar(Integer idSection, LocalDate since, LocalDate toDate) {
        String query= "DELETE FROM calendar WHERE idSection=" + idSection+" AND date BETWEEN '"+since+"' AND '"+toDate+"';";
        modifica(query);
    }

    public static void removeSectionCalendar(Section sec){
        modifica("DELETE FROM calendar WHERE idSection = "+sec.getId());
    }

    public static List<Day> readCalendarOfSection(Integer idSection) {
        List<String[]> list = lligQuery("SELECT date, workTime FROM calendar WHERE date >= CURDATE() AND idSection=" + idSection);
        List<Day> returnList = new ArrayList<>();
        for (String[] reg : list) {
            Day day = new Day(LocalDate.parse(reg[0]), Integer.parseInt(reg[1]));
            returnList.add(day);
        }
        return returnList;
    }
//HERE
    public static List<Day> readCalendarOfSection(Integer idSection, LocalDate fromDate , LocalDate toDate ) {
        List<Day> cal = new ArrayList<>();
        List<String[]> lst =lligQuery("SELECT date, workTime FROM calendar WHERE idSection ="+idSection+" AND date BETWEEN '"+fromDate+"' AND '"+toDate+"';");
        for (String[] reg : lst) {
            LocalDate date= LocalDate.parse(reg[0]);
            Integer time = Integer.parseInt(reg[1]);
            Day day= new Day(date, time);
            cal.add(day);
        }
        return cal;
    }

    private static Map<Integer, String> skill;

    public static List<TaskSkill> readTaskTypes() {
        skill = new HashMap<>();
        List<TaskSkill> list = new ArrayList<>();
        List<String[]> lst = lligTaula("taskType");
        for (String[] reg : lst) {
            TaskSkill taskType = new TaskSkill(Integer.parseInt(reg[0]), reg[1]);
            skill.put(Integer.parseInt(reg[0]), reg[1]);
            list.add(taskType);

        }
        return list;
    }

    public static List<Worker> readWorkers() {
        List<Worker> readedList = new ArrayList<>();
        List<String[]> lst = lligTaula("worker");
        for (String[] reg : lst) {

            String idWorker = reg[0];
            String userName = reg[1];
            String fullName = reg[2];
            String passwd = reg[3];
            String since = reg[4];
            String ssNum = reg[5];
            String dni = reg[6];
            String section = reg[7];
            String rank = reg[8];
            String address = reg[9];
            String telNum = reg[10];
            String mail = reg[11];
            String contact = reg[12];
            String docFolder = reg[13];
            String active = reg[14];
            String rol = reg[15];
            Worker wk = new Worker(idWorker, userName, fullName, passwd, since, ssNum, dni, section, rank, address,
                    telNum, mail, contact, docFolder, active, rol);
            readedList.add(wk);
        }
        return readedList;
    }

    public static void initStaff() {
        // generate "GENERAL" calendar if does't exist;
        LocalDate today = LocalDate.now();
        LocalDate lastDate = today;
        int nextYear = today.getYear() + 1;
        LocalDate expected = YearMonth.of(nextYear, 2).atEndOfMonth();
        String gettedDate = lligString("SELECT * FROM lastGeneratedDates ORDER BY lastDate DESC LIMIT 1;");
        if (gettedDate != null) {
            lastDate = LocalDate.parse(gettedDate);
        }
        if (lastDate.isBefore(expected)) {
            lastDate = lastDate.plusDays(1);
            for (LocalDate date = lastDate; !date.isAfter(expected); date = date.plusDays(1)) {

            }
        }
    }

    public static void modifyDay(Integer idWorker, LocalDate date, Integer time){
        modifica("UPDATE calendar SET workTime = "+time+" WHERE idWorker = "+idWorker+" AND date = '"+date+"';");
    }
    public static void modifySectionDay(Integer idSection, LocalDate date, Integer time){
        modifica("UPDATE calendar SET workTime = "+time+" WHERE idSection = "+idSection+" AND date = '"+date+"';");
    }

    public static Integer getDayWorktime(Integer idWorker, LocalDate date){
        String res=lligString("SELECT workTime FROM calendar WHERE idWorker = "+idWorker+" AND date = '"+date+"';");
        return Integer.parseInt(res);
        
    }

    public static Integer getDaySectionWorktime(Integer idSection, LocalDate date){
        String res=lligString("SELECT workTime FROM calendar WHERE idSection = "+idSection+" AND date = '"+date+"';");
        return Integer.parseInt(res);
        
    }

    public static void writeCalendar(int idWorker, List<Day> calendar) {
        for (Day day : calendar) {
            agrega("calendar", new Object[] { "idWorker", idWorker, "date", day.getDate().toString(), "workTime",
                    day.getWorkTime() });
        }
    }

    public static void writeSectionCalendar(int idSection, List<Day> calendar) {
        for (Day day : calendar) {
            agrega("calendar", new Object[] { "idSection", idSection, "date", day.getDate().toString(), "workTime",
                    day.getWorkTime() });
        }
    }

    public static RolAndId trustWorker(String username, String pwd) {

        username = username.split(" ")[0]; // to prevent SQL attack
        pwd = pwd.split(" ")[0];
        String[] reg = lligReg(
                "SELECT idWorker, workerRol FROM worker WHERE userName='" + username + "' AND password='" + pwd + "'");
        return (reg != null && reg[0] != null) ? new RolAndId(Integer.parseInt(reg[0]), reg[1]) : null;
    }

    public static void modifica(String query) {
        try {
            Connection con = DriverManager.getConnection(bdcon, us, pw);
            Statement st = con.createStatement();
            st.executeUpdate(query);
            con.close();
            if (logMode)
                bbddlog.log("Accesdb.modifica(" + query + ")");
        } catch (SQLException e) {
            String msg = ("Error en la bd -modifica-: \n" + query + "\n" + e.getErrorCode() + "-" + e.getMessage());
            if (logMode)
                bbddlog.log(msg);
            else
                System.out.println(msg);
        }

    }

    public static String lligString(String query) {
        if (logMode)
            bbddlog.log("Accesdb.lligString(" + query + ")");
        if (lligReg(query) == null)
            return null;
        return lligReg(query)[0];
    }

    public static String[] lligReg(String query) { // retorna el primer registre d'una consulta
        String[] eixida = null;
        try {

            Connection con = DriverManager.getConnection(bdcon, us, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            int cols = rs.getMetaData().getColumnCount();
            eixida = new String[cols];
            if (rs.next()) {
                for (int i = 0; i < eixida.length; i++) {
                    eixida[i] = rs.getString(i + 1);
                }
            }
            con.close();
            if (logMode)
                bbddlog.log("Accesdb.lligReg(" + query + ")");
        } catch (SQLException e) {
            String msg = "Error en la bd -lligReg-: " + e.getErrorCode() + "-" + e.getMessage();
            if (logMode)
                bbddlog.log(msg);
            else
                System.out.println(msg);
        }
        return eixida;
    }

    public static int agrega(String taula, Object[] camp_valor_camp2_valor2_) {
        // agrega a la bbdd en la taula l'objecte {camp,1, valor1, camp2, valor2....}
        boolean clau = true;
        String taules = "";
        String valors = "";
        String sep = "";
        for (Object object : camp_valor_camp2_valor2_) {
            if (clau) {
                taules += object + ",";
            } else {
                sep = (object instanceof String) ? "'" : "";
                valors += sep + object + sep + ",";
            }
            clau = !clau;
        }
        taules = taules.substring(0, taules.length() - 1); // fora última ','
        valors = valors.substring(0, valors.length() - 1);
        String query = "INSERT INTO " + taula + " (" + taules + ") VALUES (" + valors + ");";
        int idInsertat = -1;
        if (logMode)
            bbddlog.log(query);
        try {
            Connection con = DriverManager.getConnection(bdcon, us, pw);
            Statement st = con.createStatement();
            st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                idInsertat = rs.getInt(1); // Obtener el valor de la clave generada
            }
            con.close();
            if (logMode)
                bbddlog.log("Accesdb.agrega(..) " + query);
        } catch (SQLException e) {
            String msg = "Error en la bd -agrega-: " + e.getErrorCode() + "-" + e.getMessage();
            if (logMode)
                bbddlog.log(msg);
            else
                System.out.println(msg);
            return 0;

        }
        return idInsertat;

    }

    public static List<String[]> lligTaula(String taula) { // retorna la taula d'una consulta
        List<String[]> eixida = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(bdcon, us, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + taula);
            int cols = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                String[] registre = new String[cols];
                for (int i = 0; i < registre.length; i++) {
                    registre[i] = rs.getString(i + 1);
                }
                eixida.add(registre);
            }
            con.close();
            if (logMode)
                bbddlog.log("Accesdb.lligtaula(" + taula + ") ->" + "SELECT * FROM " + taula);
        } catch (SQLException e) {
            String msg = "Error en la bd -lligTaula- : " + e.getErrorCode() + "-" + e.getMessage();
            if (logMode)
                bbddlog.log(msg);
            else
                System.out.println(msg);
        }
        return eixida;
    }

    public static List<Integer> lligQuery2Integers(String query) {
        List<Integer> eixida = new ArrayList<>();
        List<String[]> l = lligQuery(query);
        try {
            for (String[] str : l) {
                eixida.add(Integer.parseInt(str[0]));
            }
        } catch (Exception e) {
            String msg = "lligQuery2Integers(..) Error obtenint una lllista de Integer a partir de la query";
            if (logMode)
                bbddlog.log(msg);
            else
                System.out.println(msg);
        }

        return eixida;
    }

    public static List<String[]> lligQuery(String query) { // retorna la taula d'una consulta
        List<String[]> eixida = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(bdcon, us, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            int cols = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                String[] registre = new String[cols];
                for (int i = 0; i < registre.length; i++) {
                    registre[i] = rs.getString(i + 1);
                }
                eixida.add(registre);
            }
            con.close();
            if (logMode)
                bbddlog.log("Accesdb.lligQuery(..)) ->" + query);
        } catch (SQLException e) {
            String msg = "Error en la bd -lligQuery-: " + e.getErrorCode() + "-" + e.getMessage();
            if (logMode)
                bbddlog.log(msg);
            else
                System.out.println(msg);
        }
        return eixida;
    }

    public static Integer toInt(String entrada) { // vull deixar el tractament d'errors agrupat
        Integer eixida = null;
        try {
            eixida = Integer.parseInt(entrada);
        } catch (ClassCastException e) {
            String msg = "Error en convertir <" + entrada + "> en tipus Integer -" + e.getMessage();
            if (logMode)
                bbddlog.log(msg);
            else
                System.out.println(msg);

            return null;
        }
        return eixida;
    }

}