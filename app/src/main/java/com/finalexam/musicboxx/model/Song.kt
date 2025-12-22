import com.google.firebase.firestore.PropertyName

// Dùng data class với giá trị mặc định để có empty constructor
data class Song(
    var id: String = "",
    var title: String = "",
    var artist: String = "",
    var album: String = "",
    var name: String = "",
    // Map trường "audioUrl" (hoặc tên khác trên Firestore của bạn)
    @get:PropertyName("audioUrl")
    @set:PropertyName("audioUrl")
    var audioUrl: String = "",

    // QUAN TRỌNG: Map trường "image_url" trên Firestore vào biến này
    // Hãy chắc chắn trên Firestore bạn đặt tên là "image_url" (chữ thường, gạch dưới)
    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var albumArtUrl: String = ""
)