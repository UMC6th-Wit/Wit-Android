package ddwu.com.mobile.finalreport.data

import java.io.Serializable
import java.util.Date

//임시 test data
data class PersonalDto (var image: Int, var title: String, var yen: String, var won: String, var star: String,  var reviewNum: Int) : Serializable {
    override fun toString() = "$title - $yen ($won) / $star($reviewNum)"
}
