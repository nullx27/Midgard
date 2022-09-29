package tech.grimm.midgard.services.models

@kotlinx.serialization.Serializable
data class Img(
    var src: String,
    var alt: String,
    var title: String,
)

@kotlinx.serialization.Serializable
data class Pod(
    var title: String,
    var id: String,
    var position: Int,
    var error: Boolean,
    var numsubpods: Int,
    var subpods: ArrayList<Subpod>,
)

@kotlinx.serialization.Serializable
data class Queryresult(
    var success: Boolean,
    var error: Boolean,
    var numpods: Int,
    var id: String,
    var related: String,
    var version: String,
    var inputstring: String,
    var pods: ArrayList<Pod>,
)

@kotlinx.serialization.Serializable
data class WolframAlphaResponse(
    var queryresult: Queryresult,
)

@kotlinx.serialization.Serializable
class Subpod(
    var title: String,
    var img: Img,
    var plaintext: String,
)