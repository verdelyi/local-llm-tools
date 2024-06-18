package localllm.ooba.requests

import kotlinx.serialization.Serializable

@Serializable
class ModelRequest(
    val model_name: String?,
    val args: ModelLoadArgs? = null,
    val settings: ModelSettings? = null
)

@Serializable
class ModelLoadArgs(
    val loader: String = "ExLlamav2_HF",
    /*// llama.cpp params
    val threads: Int = 0,
    val n_batch: Int = 512,
    val no_mmap: Boolean = false,
    val mlock: Boolean = false,
    val cache_capacity: String? = null,
    val n_gpu_layers: Int = 128,
    val n_ctx: Int = 4096*/
)

@Serializable
class ModelSettings(
    val instruction_template: String
)