package cn.gridx.phoenix.sql.scala

import java.sql.DriverManager
import scala.collection.mutable.ArrayBuffer

/**
 * Created by tao on 6/2/15.
 *
 * 参考[Paged Query](http://phoenix.apache.org/paged.html)
 */
object PageQueries {
    def ZkQurom = "ecs1.njzd.com:2181"
    def View = """"food:products""""
    def PK = """"pk""""

    def main(args: Array[String]): Unit = {
        println("Hello from Scala")
        PagedQuery("'10101'", 5)
    }

    def GetConnection(zkQurom:String) = DriverManager.getConnection("jdbc:phoenix:" + zkQurom)

    /**
     *
     * @param startPk   开始主键
     * @param count     一次查询的记录数
     */
    def PagedQuery(startPk:String, count:Int): Unit = {
        val sqlStmt = s"SELECT * FROM $View WHERE $PK > $startPk ORDER BY $PK LIMIT $count"
        println(s"\n$sqlStmt\n")

        val pstmt = GetConnection(ZkQurom).prepareStatement(sqlStmt)
        val rs = pstmt.executeQuery()

        while (rs.next) {
            val record = ArrayBuffer[String]()
            for (i <- 1 to rs.getMetaData.getColumnCount)
                record.append(rs.getString(i))

            println("\n\n\n\n")
            println(record.mkString(" ---- "))
        }

        rs.close
        pstmt.close
    }

}
