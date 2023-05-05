package de.brenner.it.server.handler

data class HttpHandlerEntry(var path: String? = null,
                            var requestMethod: String? = null,
                            var requestBody: String? = null,
                            var requestBodyFileReference: String? = null,
                            var responseStatus: Int? = null,
                            var responseBody: String? = null,
                            var responseBodyFileReference: String? = null)
