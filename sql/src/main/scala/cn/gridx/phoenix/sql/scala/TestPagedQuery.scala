package cn.gridx.phoenix.sql.scala

import java.sql.DriverManager

/**
 * Created by tao on 10/20/15.
 *
 * 测试分页查询Phoenix中的一个表
 * 使用方法：java -cp <target.jar>:<phoenix-client.jar>:<scala-library.jar> <MainClass>
 *
 */
object TestPagedQuery {
    val Driver  = "org.apache.phoenix.jdbc.PhoenixDriver"
    val ZK_CONN = "ecs2:2181"

    def main(args: Array[String]): Unit = {
        f1()
    }

    /**
     * 方法1：对主键"ROW"进行排序，每次查询5条记录，查询10次
     */
    def f1(): Unit = {
        Class.forName(Driver)
        val conn = DriverManager.getConnection(s"jdbc:phoenix:${ZK_CONN}")

        var query = """ select * from "TEST" order by "ROW" limit 5 """
        var pstmt = conn.prepareStatement(query)
        var rs    = pstmt.executeQuery
        var row   = ""  //  记录下最近一次查询到的记录的ROW的值

        // 第一次查询
        println("-------------------------------------------")
        println("# 1 ")

        while (rs.next()) {
            println(s"ROW = ${rs.getString("ROW")}, " +
                    s"C1 = ${rs.getString("C1")}, " +
                    s"C2 = ${rs.getString("C2")}, " +
                    s"C3 = ${rs.getString("C3")}")
            row = rs.getString("ROW")
        }

        // 后续的分页查询
        for (i <- 1 to 10) {
            query =
                    s""" select * from "TEST" where "ROW" > '${row}'
                   order by "ROW" limit 5  """
            pstmt = conn.prepareStatement(query)
            rs = pstmt.executeQuery

            println("\n---------------------------------------------------------------")
            println(s"# ${i} ")

            while (rs.next()) {
                println(s"ROW = ${rs.getString("ROW")}, " +
                        s"C1 = ${rs.getString("C1")}, " +
                        s"C2 = ${rs.getString("C2")}, " +
                        s"C3 = ${rs.getString("C3")}")
                row = rs.getString("ROW")
            }
        }

        rs.close
        pstmt.close
        conn.close
    }
}
