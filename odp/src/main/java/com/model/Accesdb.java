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

    //DEBUGGING:
    private static boolean local = false;
    private static boolean logMode = true;
    
    private static LogToFile bbddlog = new LogToFile("queries");
    private final static String BBDD_NAME = "odplanDDBB";
    private final static String bdcon =(local)? "jdbc:mysql://localhost:3306/"+BBDD_NAME:"jdbc:mysql://localhost:33006/" + BBDD_NAME;
    //private final static String bdcon = "jdbc:mysql://localhost:33006/"+BBDD_NAME;
    private final static String us = "root";
    private final static String pw = "root";


    public static void setLogOn() {
       logMode = true;
    }

    public static void setLogOff() {
        logMode = false;
    }

    public static List<Section> getAllSections() {
        List<Section> list = new ArrayList<>();
        List<String[]> lst = lligTaula("section");
        for (String[] reg : lst) {
            Section section = new Section(Integer.parseInt(reg[0]), reg[1]);
            list.add(section);
        }
        return list;
    }

    public static void addWorker(Worker worker) { ////
        Object[] ob = new Object[] {
            "idWorker",worker.getIdWorker(),
            "userName",worker.getUserName(),
            "fullName",worker.getFullName(),
            "password",worker.getPasswd(),
            "sinceDate",worker.getSince().toString(),
            "ss",worker.getSsNum(),
            "dni",worker.getDni(),
            "idSection",worker.getSection(),
            "idRank",worker.getRank(),
            "address",worker.getAddress(),
            "telNum",worker.getTelNum(),
            "email",worker.getMail(),
            "contact",worker.getContact(),
            "docFolder",worker.getDocFolder(),
            "active",(worker.getActive())?"YES":"NO",
            "workerType","WORKER",
            "workerRol","WORKER"
        };

        agrega("worker",ob);

    }

    public static void updateWorker(Worker worker) { ////
        String query="UPDATE worker SET";
        query+=" userName = '"+worker.getUserName();
        query+="', fullName = '"+worker.getFullName();
        query+="', password = '"+worker.getPasswd();
        query+="', sinceDate = '"+worker.getSince().toString();
        query+="', ss = '"+worker.getSsNum();
        query+="', dni = '"+worker.getDni();
        query+="', idSection = "+worker.getSection();
        query+=", idRank = "+worker.getRank();
        query+=", address = '"+worker.getAddress();
        query+="', telNum = '"+worker.getTelNum();
        query+="', email = '"+worker.getMail();
        query+="', contact = '"+worker.getContact();
        query+="', docFolder = '"+worker.getDocFolder();
        query+="', active = "+((worker.getActive())?"'YES'":"'NO'");
        query+=" WHERE idWorker = "+worker.getIdWorker();
        modifica(query);

    }

    public static void addSection(Section sec) {
        agrega(BBDD_NAME + ".section", new Object[] { "idSection", sec.getId(), "name", sec.toString() });
    }

    public static void addTask(TaskType task) {
        agrega(BBDD_NAME + ".taskType", new Object[] { "idTask", task.getId(), "name", task.toString() });
    }

    public static void addRank(Rank rank) {
        agrega(BBDD_NAME + ".rank", new Object[] { "idRank", rank.getId(), "name", rank.toString() });
    }

    public static void removeSection(Section sec) {
        modifica("DELETE FROM " + BBDD_NAME + ".section WHERE idSection = " + sec.getId());
    }

    public static void removeRank(Rank rank) {
        modifica("DELETE FROM " + BBDD_NAME + ".rank WHERE idRank = " + rank.getId());
    }

    public static void removeTask(TaskType task) {
        modifica("DELETE FROM " + BBDD_NAME + ".taskType WHERE idTask= " + task.getId());
    }

    public static boolean isTaskinUse(TaskType task) {
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

    public static void  modifyWorkerSkills(Worker worker){
        String query =  "DELETE FROM abilities WHERE idWorker = "+worker.getIdWorker();
        modifica(query);
        for (TaskType skill : worker.abilities) {
            agrega("abilities",new Object[]{"idTask",skill.getId(),"idWorker",worker.getIdWorker()});
        }
    }

    public static Map<Integer, WeekTemplate> readWeekTemplates() {
        Map<Integer, WeekTemplate> readedList = new HashMap<>();
        List<String[]> lst = lligTaula("weekTemplate"); 
        for (String[] reg : lst) {
            int idWeek = Integer.parseInt(reg[0]);
            int idWorker = Integer.parseInt(reg[1]);
            int monday = Integer.parseInt(reg[2]);
            int tuesday = Integer.parseInt(reg[3]);
            int wednesday = Integer.parseInt(reg[4]);
            int thursday = Integer.parseInt(reg[5]);
            int friday = Integer.parseInt(reg[6]);
            int saturday = Integer.parseInt(reg[7]);
            WeekTemplate wt = new WeekTemplate(monday, tuesday, wednesday, thursday, friday, saturday);
            readedList.put(idWorker, wt);
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
        List<String[]> list = lligQuery("SELECT date, workTime FROM calendar WHERE idWorker=" + idWorker);
        List<Day> returnList = new ArrayList<>();
        for (String[] reg : list) {
            Day day = new Day(LocalDate.parse(reg[0]), Integer.parseInt(reg[1]));
            returnList.add(day);
        }
        return returnList;
    }

    public static List<TaskType> readTaskTypes() {
        List<TaskType> list = new ArrayList<>();
        List<String[]> lst = lligTaula("taskType");
        for (String[] reg : lst) {
            TaskType taskType = new TaskType(Integer.parseInt(reg[0]), reg[1]);
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
            String type = reg[15];
            String rol = reg[16];
            Worker wk = new Worker(idWorker, userName, fullName, passwd, since, ssNum, dni, section, rank, address,
                    telNum, mail, contact, docFolder, active, type, rol);
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

    public static void writeCalendar(int idWorker, List<Day> calendar) {
        for (Day day : calendar) {
            agrega("calendar", new Object[] { "idWorker", idWorker, "date", day.getDate().toString(), "workTime",
                    day.getWorkTime() });
        }

    }

    public static RolAndId trustWorker(String username, String pwd) {

        username = username.split(" ")[0]; // to prevent SQL attack
        pwd = pwd.split(" ")[0];
        String[] reg = lligReg(
                "SELECT idWorker, workerRol FROM worker WHERE userName='" + username + "' AND password='" + pwd + "'");
        return (reg[0] != null) ? new RolAndId(Integer.parseInt(reg[0]), reg[1]) : null;
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
        if (lligReg(query)==null) return null;
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
            // System.out.println("Error en convertir <" + entrada + "> en tipus Integer -"
            // + e.getMessage());
            // sc.nextLine();
            return null;
        }
        return eixida;
    }

}