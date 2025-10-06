package com.comcode.utils

object EmailUtils {

    fun isHtml(content: String?): Boolean {
        if (content.isNullOrBlank()) return false

        val trimmed = content.trim().lowercase()

        // Verifica se começa com <!doctype html> ou contém uma tag básica de HTML
        return trimmed.startsWith("<!doctype html") ||
                Regex("<(html|body|table|div|p|span|head|meta|title)(\\s|>)")
                    .containsMatchIn(trimmed)
    }

    fun buildEmailTemplate(content: String): String {
        return """
            <!DOCTYPE html>
            <html lang="pt-br">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Bem-vindo ao ComCode!</title>
                <style>
                    body { margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f7f6; color: #333; }
                    .container { width: 100%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
                    .header { background-color: #2c3e50; padding: 30px 20px; text-align: center; }
                    .header img { max-width: 150px; }
                    .content { padding: 40px 30px; line-height: 1.7; text-align: left; }
                    .content h1 { color: #2c3e50; font-size: 24px; }
                    .cta-button { display: inline-block; background-color: #3498db; color: #ffffff !important; padding: 12px 25px; text-decoration: none; border-radius: 5px; margin: 20px 0; font-weight: bold; }
                    .footer { background-color: #ecf0f1; padding: 20px; text-align: center; font-size: 12px; color: #7f8c8d; }
                    .footer a { color: #3498db; text-decoration: none; margin: 0 5px; }
                </style>
            </head>
            <body>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-color: #f4f7f6;">
                    <tr>
                        <td align="center">
                            <table class="container" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td class="header">
                                        <img src="https://placehold.co/150x50/2c3e50/ffffff?text=ComCode" alt="ComCode Logo">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="content">
                                        $content
                                    </td>
                                </tr>
                                <tr>
                                    <td class="footer">
                                        <p>Atenciosamente,<br>Equipe ComCode</p>
                                        <p>
                                            <a href="#">Facebook</a> |
                                            <a href="#">Instagram</a> |
                                            <a href="#">LinkedIn</a>
                                        </p>
                                        <p>&copy; ComCode. Todos os direitos reservados.</p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
        """.trimIndent()
    }
}
