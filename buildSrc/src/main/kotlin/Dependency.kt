import org.gradle.api.artifacts.dsl.DependencyHandler

data class Dependency(
    val groupName: String,
    val version: String = "",
    val type: Type = Type.DEFAULT
) {
    fun get(handler: DependencyHandler) {
        val notation = "$groupName:$version"
        val dependency = when (this.type) {
            Type.PLATFORM -> Type.DEFAULT.get to handler.platform(notation)
            else -> this.type.get to notation
        }

        handler.add(dependency.first, dependency.second)
    }

    fun get(handler: DependencyHandler, type: Type) {
        val notation = "$groupName:$version"
        val dependency = when (type) {
            Type.PLATFORM -> Type.DEFAULT.get to handler.platform(notation)
            else -> type.get to notation
        }

        handler.add(dependency.first, dependency.second)
    }
}

fun getAll(handler: DependencyHandler, list: List<Dependency>) {
    list.forEach { dependency ->
        dependency.get(handler)
    }
}
