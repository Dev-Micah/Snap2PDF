package com.micahnyabuto.snap2pdf.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micahnyabuto.snap2pdf.core.data.local.Document
import com.micahnyabuto.snap2pdf.core.data.repository.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DocumentViewModel(private val repository: DocumentRepository) : ViewModel() {
    private val _documents = MutableStateFlow<List<Document>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    val documents: StateFlow<List<Document>> = _documents

    private val _uiState = MutableStateFlow(DocumentUiState())

    val uiState: StateFlow<DocumentUiState> = _uiState


    fun loadDocuments() {
        viewModelScope.launch {
            repository.allDocuments().collect { documentList ->
                _documents.value = documentList
            }
        }
    }


    fun addDocument(document: Document) {
        viewModelScope.launch {
            repository.addDocument(
                document = document
            )
            loadDocuments()
        }
    }

    fun deleteDocument(document: Document) {
        viewModelScope.launch {
            repository.deleteDocument(document)
            loadDocuments()
        }
    }


    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val filteredDocuments: StateFlow<List<Document>> = combine(
        repository.allDocuments(),
        _searchQuery
    ) { documents, query ->
        if (query.isBlank()) documents
        else documents.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    data class DocumentUiState(
        val isLoading: Boolean = false,
        val success: List<Document> = emptyList(),
        val error: String? = null,
        val empty: Boolean = false
    )
}
