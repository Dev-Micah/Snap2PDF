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

    private val _uiState = MutableStateFlow(DocumentUiState()) // isLoading will now default to true

    val uiState: StateFlow<DocumentUiState> = _uiState


    fun loadDocuments() {
        viewModelScope.launch {
            // You might want to explicitly set isLoading = true here if it wasn't the default
            // _uiState.value = _uiState.value.copy(isLoading = true)
            repository.allDocuments().collect { documentList ->
                _documents.value = documentList
                // After loading, update the uiState to reflect completion and data
                _uiState.value = _uiState.value.copy(
                    isLoading = false, 
                    success = documentList, 
                    empty = documentList.isEmpty(),
                    error = null // Clear any previous error
                )
            }
            // TODO: Add error handling for the repository call, e.g., a catch block
            // .catch { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message) }
        }
    }


    fun addDocument(document: Document) {
        viewModelScope.launch {
            repository.addDocument(
                document = document
            )
            loadDocuments() // This will refresh the state including isLoading updates
        }
    }

    fun deleteDocument(document: Document) {
        viewModelScope.launch {
            repository.deleteDocument(document)
            loadDocuments() // This will refresh the state including isLoading updates
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
        val isLoading: Boolean = true, // Changed default to true
        val success: List<Document> = emptyList(),
        val error: String? = null,
        val empty: Boolean = false
    )
}
