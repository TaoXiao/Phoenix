package cn.gridx.phoenix.sql.scala

import java.sql.{ResultSet, PreparedStatement, DriverManager}

/**
 * Created by tao on 10/20/15.
 *
 * 通过JDBC SQL的方式来查询Phoenix中的table
 * 使用方法：java -cp <target.jar>:<phoenix-client.jar>:<scala-library.jar> <MainClass>
 *
 */
object TestJDBC {
    val Driver  = "org.apache.phoenix.jdbc.PhoenixDriver"
    val ZK_CONN = "ecs2:2181" // 即使有多个zk node，这里也只需要写一个node就行了
    val QUERY   = """select * from "TEST" where "ROW" in
              ('row-15', 'row-30', 'row-45', 'row-no')  """

    def main(args: Array[String]): Unit = {
        Class.forName(Driver)
        val conn = DriverManager.getConnection(s"jdbc:phoenix:${ZK_CONN}")

        val pstmt: PreparedStatement = conn.prepareStatement(QUERY)
        val rs: ResultSet = pstmt.executeQuery

        while (rs.next())
            println(s"ROW = ${rs.getString("ROW")}, " +
                    s"C1 = ${rs.getString("C1")}, " +
                    s"C2 = ${rs.getString("C2")}, " +
                    s"C3 = ${rs.getString("C3")}")

        rs.close
        pstmt.close
        conn.close
    }
}
