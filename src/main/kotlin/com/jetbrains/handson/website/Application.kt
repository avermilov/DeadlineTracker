package com.jetbrains.handson.website

import freemarker.cache.*
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.receiveParameters
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import java.io.File


val blogEntries: MutableList<BlogEntry> = mutableListOf()

const val DEADLINES_PATH = "src/main/resources/text/deadlines.txt"
const val REMOVEALL_PATH = "src/main/resources/files/removeall.html"
const val ADD_PATH = "src/main/resources/files/added.html"
const val REMOVE_PATH = "src/main/resources/files/removed.html"

fun main(args: Array<String>) {
    loadBlogEntries()
    io.ktor.server.netty.EngineMain.main(args)
}

fun loadBlogEntries() {
    val content = File(DEADLINES_PATH).readLines()
    for (i in content.indices step 2) {
        blogEntries.add(BlogEntry(content[i], content[i + 1]))
    }
}

fun saveBlogEntries() {
    File(DEADLINES_PATH).printWriter().use { out ->
        blogEntries.forEach {
            out.println(it.headline)
            out.println(it.body)
        }
    }
}

fun Application.module() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        outputFormat = HTMLOutputFormat.INSTANCE
    }
    routing {
        static("/static") {
            resources("files")
        }

        get("/") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("entries" to blogEntries), ""))
        }

        post("/submit") {
            val params = call.receiveParameters()
            val headline = params["headline"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val body = params["body"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            if (headline.isBlank())
                call.respond(HttpStatusCode.BadRequest)
            val newEntry = BlogEntry(headline.trim(), body.trim())
            blogEntries.add(0, newEntry)
            saveBlogEntries()

            call.respondText(File(ADD_PATH).readText(), ContentType.Text.Html)
        }

        post("/removedeadline") {
            val params = call.receiveParameters()
            val index = params["deleteIndex"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val parsed = index.toIntOrNull()
            if (index.isBlank() || parsed == null || parsed < 0 || parsed > blogEntries.size)
                call.respond(HttpStatusCode.BadRequest)
            blogEntries.removeAt(parsed!!)
            saveBlogEntries()

            call.respondText(File(REMOVE_PATH).readText(), ContentType.Text.Html)
        }

        post("/removeall") {
            blogEntries.clear()
            call.respondText(File(REMOVEALL_PATH).readText(), ContentType.Text.Html)
        }
    }
}
