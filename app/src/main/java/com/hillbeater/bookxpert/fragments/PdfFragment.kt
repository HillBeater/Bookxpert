package com.hillbeater.bookxpert.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.webkit.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hillbeater.bookxpert.R
import com.hillbeater.bookxpert.viewModel.PdfFragmentViewModel

class PdfFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private val viewModel: PdfFragmentViewModel by viewModels()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pdf, container, false)

        webView = view.findViewById(R.id.webView)
        progressBar = view.findViewById(R.id.progressBar)

        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                viewModel.setLoading(true)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                viewModel.setLoading(false)
            }

            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError?
            ) {
                viewModel.setLoading(false)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.pdfUrl.observe(viewLifecycleOwner, Observer { rawUrl ->
            val googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=$rawUrl"
            webView.loadUrl(googleDocsUrl)
        })

        val pdfUrl = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf"
        viewModel.loadPdf(pdfUrl)

        return view
    }
}
