package com.whiskey.entity

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import java.io.Serializable
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

@Suppress("UNCHECKED_CAST", "unused")
class StringArrayType : UserType {

    @Throws(HibernateException::class)
    override fun assemble(cached: Serializable, owner: Any): Any? {
        return deepCopy(cached)
    }

    @Throws(HibernateException::class)
    override fun deepCopy(value: Any?): Any? {
        return value
    }

    @Throws(HibernateException::class)
    override fun disassemble(value: Any): Serializable {
        return deepCopy(value) as Array<String>
    }

    @Throws(HibernateException::class)
    override fun equals(x: Any?, y: Any?): Boolean {
        return if (x == null) {
            y == null
        } else x == y
    }

    @Throws(HibernateException::class)
    override fun hashCode(x: Any): Int {
        return x.hashCode()
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeGet(
        resultSet: ResultSet,
        names: Array<String>,
        sharedSessionContractImplementor: SharedSessionContractImplementor,
        o: Any
    ): Any? {
        if (resultSet.wasNull()) {
            return null
        }
        if (resultSet.getArray(names[0]) == null) {
            return arrayOfNulls<String>(0)
        }
        val array: java.sql.Array = resultSet.getArray(names[0])
        return array.array
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(
        statement: PreparedStatement,
        value: Any?,
        index: Int,
        sharedSessionContractImplementor: SharedSessionContractImplementor
    ) {
        val connection: Connection = statement.connection
        if (value == null) {
            statement.setNull(index, SQL_TYPES[0])
        } else {
            val castObject = value as Array<String>
            val array: java.sql.Array = connection.createArrayOf("text", castObject)
            statement.setArray(index, array)
        }
    }

    override fun isMutable(): Boolean {
        return true
    }

    @Throws(HibernateException::class)
    override fun replace(original: Any, target: Any?, owner: Any): Any {
        return original
    }

    override fun returnedClass(): Class<Array<String>> {
        return Array<String>::class.java
    }

    override fun sqlTypes(): IntArray {
        return intArrayOf(Types.ARRAY)
    }

    companion object {
        private val SQL_TYPES = intArrayOf(Types.ARRAY)
    }
}
