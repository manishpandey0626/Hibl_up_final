package smsinfosolutions.ind.hibl.database

/**
 * Created by Manish on 27-Jan-21.
 */
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.healthymantra.piousvision.utilities.*

class DatabaseHelper(var context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    1
) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "CREATE TABLE " + TABLE_USER + " (" + PROPOSAL_NO + " VARCHAR(256) PRIMARY KEY, " + PROPOSAL_IMG + " VARCHAR(5000), " + AADHAR_NO + " VARCHAR(12), " + AADHAR_IMG + " VARCHAR(5000), " + NO_OF_ANIMAL + " VARCHAR(256), " + AREA + " VARCHAR(256), " + CATEGORY + " VARCHAR(256), " + DURATION + " VARCHAR(256), " + AREA_NAME + " VARCHAR(256), " + CATEGORY_NAME + " VARCHAR(256), " + DURATION_NAME + " VARCHAR(256), " + CITY_ID + " VARCHAR(256), " + CITY_NAME + " VARCHAR(256), " + HOSPITAL_ID + " VARCHAR(256), " + HOSPITAL_NAME + " VARCHAR(1000), " + WPTD + " VARCHAR(10), " + STATUS + " VARCHAR(1), " + AGE + " VARCHAR(3))"

        val animal_table =
            "create table animal_detail(proposal_no varchar(256),tag_no varchar(256),animal_type varchar(256),animal_type_code varchar(256),sum_insured varchar(256), PRIMARY KEY(proposal_no,tag_no))"
        val animal_images =
            "create table animal_images(proposal_no varchar(256),tag_no varchar(256),file_name varchar(256),file varchar(5000), PRIMARY KEY(proposal_no,tag_no,file_name))"
        val premium_detail =
            "create table premium_detail(proposal_no varchar(256),beneficiary_share varchar(256), central_share varchar(256), state_share varchar(256), total_premium varchar(256), PRIMARY KEY(proposal_no))"
        db?.execSQL(createTable)
        db?.execSQL(animal_table)
        db?.execSQL(animal_images)
        db?.execSQL(premium_detail)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER)
        db.execSQL("DROP TABLE IF EXISTS " + "animal_detail")
        onCreate(db)
    }

    fun insertData(user: UserData): Long {
        var output: Long
        val database = this.writableDatabase
        val contentValues = ContentValues()
        // contentValues.put(PROPOSAL_NO, user.proposal_no)
        contentValues.put(PROPOSAL_IMG, user.proposal_img)
        contentValues.put(AADHAR_NO, user.aadhar_no)
        contentValues.put(AADHAR_IMG, user.aadhar_img)
        contentValues.put(NO_OF_ANIMAL, user.no_of_animal)
        contentValues.put(CATEGORY, user.category)
        contentValues.put(AREA, user.area)
        contentValues.put(DURATION, user.duration)
        contentValues.put(CATEGORY_NAME, user.category_name)
        contentValues.put(AREA_NAME, user.area_name)
        contentValues.put(DURATION_NAME, user.duration_name)

        contentValues.put(CITY_ID, user.city_id)
        contentValues.put(CITY_NAME, user.city_name)
        contentValues.put(HOSPITAL_ID, user.hospital_id)
        contentValues.put(HOSPITAL_NAME, user.hospital_name)
        contentValues.put(WPTD, user.wptd)
        contentValues.put(AGE,user.age)

        val whereclause = "$PROPOSAL_NO = ?"
        val whereargs = arrayOf(user.proposal_no)
        val result = database.update(TABLE_USER, contentValues, whereclause, whereargs)

        if (result == 0) {
            contentValues.put(PROPOSAL_NO, user.proposal_no)
            val insert_result = database.insert(TABLE_USER, null, contentValues)
            output = insert_result
        } else {
            output = result.toLong()
        }

        return output
    }

    fun delete(proposal_no:String)
    {

        val whereclause = "$PROPOSAL_NO IN($proposal_no)"
        //val whereargs = arrayOf(proposal_no)
        val database = this.writableDatabase

       val result= database.delete(TABLE_USER,whereclause,null)
        database.delete("animal_detail",whereclause,null)
        database.delete("animal_images",whereclause,null)
        database.delete("premium_detail",whereclause,null)
    }

    fun deleteAnimal(proposal_no: String,tag_no: String):Int
    {
        val whereclause = "proposal_no=? and tag_no=?"
        val whereargs = arrayOf(proposal_no,tag_no)
        val database = this.writableDatabase

        val result=database.delete("animal_detail",whereclause,whereargs)
        database.delete("animal_images",whereclause,whereargs)
        return result

    }
    fun deleteAnimalImage(proposal_no: String,tag_no: String,file_name:String):Int
    {
        val whereclause = "proposal_no=? and tag_no=? and file_name=?"
        val whereargs = arrayOf(proposal_no,tag_no,file_name)
        val database = this.writableDatabase


        val result= database.delete("animal_images",whereclause,whereargs)
        return result

    }

    fun insertAnimalDetail(animal: AnimalDetail): Long {
        var output: Long
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("animal_type", animal.animal_type)
        contentValues.put("animal_type_code", animal.animal_type_code)
        contentValues.put("sum_insured", animal.sum_insured)

        val whereclause = "proposal_no = ? and tag_no= ?"
        val whereargs = arrayOf(animal.proposal_no, animal.tag_no)
        val result = database.update("animal_detail", contentValues, whereclause, whereargs)

        if (result == 0) {
            contentValues.put("proposal_no", animal.proposal_no)
            contentValues.put("tag_no", animal.tag_no)

            val insert_result = database.insert("animal_detail", null, contentValues)
            output = insert_result
        } else {
            output = result.toLong()
        }

        return output
    }

    fun insertAnimalImages(animal: AnimalImages): Long {
        var output: Long
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("file", animal.file)


        val whereclause = "proposal_no = ? and tag_no= ? and file_name=?"
        val whereargs = arrayOf(animal.proposal_no, animal.tag_no, animal.file_name)
        val result = database.update("animal_images", contentValues, whereclause, whereargs)

        if (result == 0) {
            contentValues.put("proposal_no", animal.proposal_no)
            contentValues.put("tag_no", animal.tag_no)
            contentValues.put("file_name", animal.file_name)

            val insert_result = database.insert("animal_images", null, contentValues)
            output = insert_result
        } else {
            output = result.toLong()
        }

        return output
    }

    fun insertPremiumDetail(premium: PremiumDetail): Long {
        var output: Long
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("beneficiary_share", premium.beneficiary_share)
        contentValues.put("central_share", premium.central_share)
        contentValues.put("state_share", premium.state_share)
        contentValues.put("total_premium", premium.total_premius)
        val whereclause = "proposal_no = ?"
        val whereargs = arrayOf(premium.proposal_no)
        val result = database.update("premium_detail", contentValues, whereclause, whereargs)

        if (result == 0) {
            contentValues.put("proposal_no", premium.proposal_no)

            val insert_result = database.insert("premium_detail", null, contentValues)
            output = insert_result
        } else {
            output = result.toLong()
        }

        return output
    }

    fun updateStatus(proposal_no: String,status:String): Int {

        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(STATUS, status)
        val whereclause = "$PROPOSAL_NO = ?"
        val whereargs = arrayOf(proposal_no)
        val result = database.update(TABLE_USER, contentValues, whereclause, whereargs)
        return result
    }

    fun getProposalList(status: String="N"): MutableList<SendUserData> {
        val list: MutableList<SendUserData> = ArrayList()
        val db = this.readableDatabase

        val query = "Select * from $TABLE_USER WHERE ifnull($STATUS,'N') = '$status'"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val status=result.getString(result.getColumnIndex(STATUS))
                val proposal_no = result.getString(result.getColumnIndex(PROPOSAL_NO))
                val proposal_img = result.getString(result.getColumnIndex(PROPOSAL_IMG))
                val aadhar_no = result.getString(result.getColumnIndex(AADHAR_NO))
                val aadhar_img = result.getString(result.getColumnIndex(AADHAR_IMG))
                val no_of_animal = result.getString(result.getColumnIndex(NO_OF_ANIMAL))
                val category = result.getString(result.getColumnIndex(CATEGORY))
                val area = result.getString(result.getColumnIndex(AREA))
                val duratioin = result.getString(result.getColumnIndex(DURATION))
                val category_name = result.getString(result.getColumnIndex(CATEGORY_NAME))
                val area_name = result.getString(result.getColumnIndex(AREA_NAME))
                val duratioin_name = result.getString(result.getColumnIndex(DURATION_NAME))
                val city_id = result.getString(result.getColumnIndex(CITY_ID))
                val city_name = result.getString(result.getColumnIndex(CITY_NAME))
                val hospital_id = result.getString(result.getColumnIndex(HOSPITAL_ID))
                val hospital_name = result.getString(result.getColumnIndex(HOSPITAL_NAME))
                val wptd = result.getString(result.getColumnIndex(WPTD))
                val age = result.getString(result.getColumnIndex(AGE))
                val user = SendUserData(
                    proposal_no,
                    proposal_img,
                    aadhar_no,
                    aadhar_img,
                    no_of_animal,
                    category,
                    area,
                    duratioin,
                    category_name,
                    area_name,
                    duratioin_name,
                    wptd,
                    city_id,
                    city_name,
                    hospital_name,
                    hospital_id,
                    false,
                    age
                )


                list.add(user)
            } while (result.moveToNext())
        }
        return list
    }

    fun readData(proposal_no: String): UserData? {
        var user: UserData? = null
        val db = this.readableDatabase
        val query = "Select * from $TABLE_USER WHERE $PROPOSAL_NO=$proposal_no"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {

            val proposal_no = result.getString(result.getColumnIndex(PROPOSAL_NO))
            val proposal_img = result.getString(result.getColumnIndex(PROPOSAL_IMG))
            val aadhar_no = result.getString(result.getColumnIndex(AADHAR_NO))
            val aadhar_img = result.getString(result.getColumnIndex(AADHAR_IMG))
            val no_of_animal = result.getString(result.getColumnIndex(NO_OF_ANIMAL))
            val category = result.getString(result.getColumnIndex(CATEGORY))
            val area = result.getString(result.getColumnIndex(AREA))
            val duration = result.getString(result.getColumnIndex(DURATION))
            val category_name = result.getString(result.getColumnIndex(CATEGORY_NAME))
            val area_name = result.getString(result.getColumnIndex(AREA_NAME))
            val duratioin_name = result.getString(result.getColumnIndex(DURATION_NAME))
            val city_id = result.getString(result.getColumnIndex(CITY_ID))
            val city_name = result.getString(result.getColumnIndex(CITY_NAME))
            val hospital_id = result.getString(result.getColumnIndex(HOSPITAL_ID))
            val hospital_name = result.getString(result.getColumnIndex(HOSPITAL_NAME))
            val wptd = result.getString(result.getColumnIndex(WPTD))
            val age = result.getString(result.getColumnIndex(AGE))
            user = UserData(
                proposal_no,
                proposal_img,
                aadhar_no,
                aadhar_img,
                no_of_animal,
                category,
                area,
                duration,
                category_name,
                area_name,
                duratioin_name,
                wptd,
                city_id,
                city_name,
                hospital_name,
                hospital_id,
                age

            )


        }
        return user
    }


    fun readAnimalDetail(proposal_no: String): MutableList<AnimalDetail> {
        val list: MutableList<AnimalDetail> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from animal_detail where proposal_no=$proposal_no"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val proposal_no = result.getString(result.getColumnIndex("proposal_no"))
                val tag_no = result.getString(result.getColumnIndex("tag_no"))
                val animal_type = result.getString(result.getColumnIndex("animal_type"))
                val animal_type_code = result.getString(result.getColumnIndex("animal_type_code"))
                val sum_insured = result.getString(result.getColumnIndex("sum_insured"))
                val user =
                    AnimalDetail(proposal_no, tag_no, animal_type, animal_type_code, sum_insured)

                list.add(user)
            } while (result.moveToNext())
        }
        return list
    }

    fun readAnimalImages(proposal_no: String, tag_no: String): MutableList<AnimalImages> {
        val list: MutableList<AnimalImages> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from animal_images where proposal_no=$proposal_no and tag_no=$tag_no"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val proposal_no = result.getString(result.getColumnIndex("proposal_no"))
                val tag_no = result.getString(result.getColumnIndex("tag_no"))
                val file_name = result.getString(result.getColumnIndex("file_name"))
                val file = result.getString(result.getColumnIndex("file"))
                val user = AnimalImages(proposal_no, tag_no, file_name, file)
                list.add(user)
            } while (result.moveToNext())
        }
        return list
    }

    fun imageCount(proposal_no: String, tag_no: String): Int {
        var cnt = -1
        val db = this.readableDatabase
        val query =
            "Select count(1) as cnt from animal_images where proposal_no=$proposal_no and tag_no=$tag_no"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            cnt = result.getInt(result.getColumnIndex("cnt"))
        }
        return cnt
    }

    fun pendingCount(status: String="N"):Int
    {
        var cnt = 0
        val db = this.readableDatabase
        val query =
            "Select count(1) as cnt from $TABLE_USER WHERE ifnull($STATUS,'N') = '$status'"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            cnt = result.getInt(result.getColumnIndex("cnt"))
        }
        return cnt
    }

    fun readPremiumDetail(proposal_no: String): PremiumDetail? {
       var  premium_detail:PremiumDetail
        val db = this.readableDatabase
        val query = "Select * from premium_detail where proposal_no=$proposal_no"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {

                val proposal_no = result.getString(result.getColumnIndex("proposal_no"))
                val beneficiary_share = result.getString(result.getColumnIndex("beneficiary_share"))
                val central_share = result.getString(result.getColumnIndex("central_share"))
                val state_share = result.getString(result.getColumnIndex("state_share"))
                val total_premium = result.getString(result.getColumnIndex("total_premium"))
            premium_detail = PremiumDetail(proposal_no,beneficiary_share,central_share,state_share,total_premium)
            return premium_detail
        }
        return null;
    }


    fun sumInsured(proposal_no: String): Double{
        var total=0.0
        val list: MutableList<AnimalDetail> = ArrayList()
        val db = this.readableDatabase
        val query = "Select sum(sum_insured) as total from animal_detail where proposal_no=$proposal_no"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
          total=result.getDouble(result.getColumnIndex("total"))
        }
        return total
    }



    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "hiblDB.db"

        //UserData table
        val TABLE_USER = "UserData"
        val PROPOSAL_NO = "proposal_no"
        val PROPOSAL_IMG = "proposal_img"
        val AADHAR_NO = "aadhar_no"
        val AADHAR_IMG = "aadhar_img"
        val NO_OF_ANIMAL = "no_of_animal"
        val CATEGORY = "category"
        val AREA = "area"
        val DURATION = "duration"
        val CATEGORY_NAME = "category_name"
        val AREA_NAME = "area_name"
        val DURATION_NAME = "duration_name"
        val CITY_ID = "city_id"
        val CITY_NAME = "city_name"
        val HOSPITAL_ID = "hospital_id"
        val HOSPITAL_NAME = "hospital_name"
        val WPTD = "wptd"
        val STATUS = "status"
        val AGE="age"


    }
}