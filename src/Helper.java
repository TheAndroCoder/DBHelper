import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Helper {
    private int count=0;
    private String query=null;
    //private Connection conn = null;
    private PreparedStatement stmt=null;
    //private StringBuilder queryBuffer=null;
    private Map<Integer,String> map=null;// stores index of param and corresponding data string representation
    public Helper(PreparedStatement stmt,String query)throws Exception{
        this.query=query;
        this.stmt=stmt;

        if(stmt==null || query==null || query.equals(""))throw new Exception("Invalid Or null Parameters");
        //this.queryBuffer=new StringBuilder(query);
        for(char ch : query.toCharArray()){
            if(ch=='?')count++;
        }
        map=new HashMap<>();
    }

    public ResultSet executeQuery() throws SQLException {
        if(count!=map.size())throw new SQLException(count+" params declared in query but "+map.size()+" values set!!");
        if(stmt!=null){
            return stmt.executeQuery();
        }
        return null;
    }
    public int executeUpdate() throws SQLException{
        if(count!=map.size())throw new SQLException(count+" params declared in query but "+map.size()+" values set!!");
        if(stmt!=null){
            return stmt.executeUpdate();
        }
        return -1;
    }

    public <T> void setValue(int index,T data)throws SQLException{
        if(data instanceof Integer){
            stmt.setInt(index,(int)data);
            map.put(index,String.valueOf(data));
        }else if(data instanceof Date){
            stmt.setDate(index,(Date)data);
            map.put(index,data.toString());
        }else if(data instanceof String){
            stmt.setString(index,(String)data);
            map.put(index,(String)data);
        }else{
            throw new SQLException("Unsupported Datatype");
        }
    }

    public String getPreparedQuery() {
        int c=0;
        String s="";
        for (int i = 0; i < query.length(); i++) {
            if(query.charAt(i)=='?'){
                c++;
                s+=map.getOrDefault(c,"?");
            }else{
                s+=query.charAt(i);
            }
        }
        return s;
    }
}
