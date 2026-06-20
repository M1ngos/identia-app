package com.identia.app.ml

import kotlinx.coroutines.runBlocking
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class OnnxEngineTest {

    // Tiny ONNX model: out = a + b, both float tensors of shape [2] (opset 13).
    // Generated with onnx.helper; embedded as base64 to keep the test self-contained.
    private val addModel: ByteArray = Base64.getDecoder().decode(
        "CAg6UgoQCgFhCgFiEgNvdXQiA0FkZBIJYWRkX2dyYXBoWg8KAWESCgoICAESBAoCCAJa" +
            "DwoBYhIKCggIARIECgIIAmIRCgNvdXQSCgoICAESBAoCCAJCBAoAEA0=",
    )

    @Test
    fun runsRealInferenceOnAddModel() = runBlocking {
        createOnnxSession(addModel).use { session ->
            val outputs = session.run(
                mapOf(
                    "a" to OnnxValue(floatArrayOf(1f, 2f), longArrayOf(2)),
                    "b" to OnnxValue(floatArrayOf(3f, 4f), longArrayOf(2)),
                ),
            )
            val out = outputs.getValue("out")
            assertContentEquals(longArrayOf(2), out.shape)
            assertContentEquals(floatArrayOf(4f, 6f), out.data)
        }
    }

    @Test
    fun reportsOutputShape() = runBlocking {
        createOnnxSession(addModel).use { session ->
            val outputs = session.run(
                mapOf(
                    "a" to OnnxValue(floatArrayOf(0f, 0f), longArrayOf(2)),
                    "b" to OnnxValue(floatArrayOf(0f, 0f), longArrayOf(2)),
                ),
            )
            assertEquals(2L, outputs.getValue("out").flatSize)
        }
    }
}
