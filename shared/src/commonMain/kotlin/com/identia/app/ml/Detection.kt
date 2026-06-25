package com.identia.app.ml

/** Axis-aligned box in the **original image's** pixel coordinates. */
data class BoundingBox(val x1: Float, val y1: Float, val x2: Float, val y2: Float) {
    val width: Float get() = x2 - x1
    val height: Float get() = y2 - y1
    val area: Float get() = (width).coerceAtLeast(0f) * (height).coerceAtLeast(0f)
}

/** One detected ID element: which field, how confident, and where. */
data class Detection(
    val classId: Int,
    val label: String,
    val score: Float,
    val box: BoundingBox,
)

/**
 * Class labels for the YOLO11 ID-card detector (`best_recent.onnx`), in the model's
 * output channel order. Index = class id in [Detection.classId].
 */
val ID_CARD_CLASSES: List<String> = listOf(
    "front_card",
    "back_card",
    "name",
    "id_number",
    "dob",
    "pob",
    "father_name",
    "mother_name",
    "valid",
    "poi",
    "sex",
    "address",
    "doi",
    "photo",
)
