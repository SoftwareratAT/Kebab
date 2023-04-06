rootProject.name = "Kebab"
include (
        "kebab-api",
        "kebab-server",
        "kebab-common"
)

findProject(":kebab-api")?.name = "kebab-api"
findProject(":kebab-server")?.name = "kebab-server"
findProject(":kebab-common")?.name = "kebab-common"
