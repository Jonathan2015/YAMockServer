package de.brenner.it.mockserver.handler

data class HttpPath(var path: String? = null,
                    var requestMethod: String? = null,
                    var requestBody: String? = null,
                    var requestBodyFileReference: String? = null,
                    var responseStatus: Int? = null,
                    var responseBody: String? = null,
                    var responseBodyFileReference: String? = null)
