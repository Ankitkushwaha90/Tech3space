package com.example.project_try9.ui.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MdxLessonScreen(
    content: String,
    modifier: Modifier = Modifier
) {
    // Convert Markdown → HTML
    val htmlBody = remember(content) {
        val extensions = listOf(
            TablesExtension.create(),
            StrikethroughExtension.create()
        )
        val parser = Parser.builder()
            .extensions(extensions)
            .build()
        val renderer = HtmlRenderer.builder()
            .extensions(extensions)
            .build()

        val document = parser.parse(content)
        renderer.render(document)
    }

    val fullHtml = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            
            <!-- KaTeX CSS -->
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.16.11/dist/katex.min.css">
            
            <style>
                * { box-sizing: border-box; }
                body {
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                    line-height: 1.7;
                    color: #24292e;
                    padding: 16px;
                    margin: 0;
                    background: #ffffff;
                    overflow-wrap: anywhere;
                }
                h1, h2, h3, h4, h5, h6 {
                    color: #1a1a1a;
                    line-height: 1.3;
                    margin-top: 1.6em;
                    margin-bottom: 0.6em;
                }
                h1 { font-size: 1.8em; }
                h2 { font-size: 1.5em; }
                h3 { font-size: 1.25em; }
                p { margin: 0.9em 0; }
                a { color: #0969da; text-decoration: none; }
                img, video, iframe {
                    max-width: 100%;
                    height: auto;
                    border-radius: 6px;
                }
                pre {
                    padding: 14px;
                    overflow-x: auto;
                    background: #f6f8fa;
                    border-radius: 8px;
                    font-size: 0.9em;
                    line-height: 1.45;
                }
                code {
                    font-family: ui-monospace, SFMono-Regular, "SF Mono", Menlo, Consolas, monospace;
                    background: #f0f2f4;
                    border-radius: 4px;
                    padding: 0.2em 0.4em;
                    font-size: 0.9em;
                }
                pre code {
                    background: transparent;
                    padding: 0;
                }
                table {
                    display: block;
                    width: 100%;
                    overflow-x: auto;
                    border-collapse: collapse;
                    margin: 1.2em 0;
                }
                th, td {
                    border: 1px solid #d0d7de;
                    padding: 8px 12px;
                    text-align: left;
                }
                th { background: #f6f8fa; font-weight: 600; }
                blockquote {
                    margin: 1em 0;
                    padding: 0 1em;
                    border-left: 4px solid #d0d7de;
                    color: #57606a;
                }
                ul, ol { padding-left: 1.6em; }
                li { margin: 0.35em 0; }
                hr {
                    border: none;
                    border-top: 1px solid #d0d7de;
                    margin: 2em 0;
                }

                /* KaTeX tweaks */
                .katex { font-size: 1.1em; }
                .katex-display {
                    margin: 1.2em 0;
                    overflow-x: auto;
                    overflow-y: hidden;
                }
            </style>
        </head>
        <body>
            $htmlBody

            <!-- KaTeX JS -->
            <script defer src="https://cdn.jsdelivr.net/npm/katex@0.16.11/dist/katex.min.js"></script>
            <script defer src="https://cdn.jsdelivr.net/npm/katex@0.16.11/dist/contrib/auto-render.min.js"
                onload="renderMathInElement(document.body, {
                    delimiters: [
                        {left: '\$\$', right: '\$\$', display: true},
                        {left: '\$', right: '\$', display: false},
                        {left: '\\\\[', right: '\\\\]', display: true},
                        {left: '\\\\(', right: '\\\\)', display: false}
                    ],
                    throwOnError: false
                });">
            </script>
        </body>
        </html>
    """.trimIndent()

    key(content) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()

                    // JavaScript is required for KaTeX
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true

                    // Optional: better performance
                    settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT

                    loadDataWithBaseURL(
                        "https://tech3space.com/",
                        fullHtml,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            },
            update = { /* content change is handled by key() */ }
        )
    }
}