package com.sadikul.currencyexchange.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.data.repository.FakeCurrencyRepoImpl
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCurrenciesTest {
    private lateinit var useCase: GetCurrenciesUseCase
    lateinit var currencyrepo: FakeCurrencyRepoImpl

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        currencyrepo = FakeCurrencyRepoImpl()
        useCase = GetCurrenciesUseCase(currencyrepo)
    }
    @Test
    fun checkUseCaseProvidingDataproperly() = runBlocking {
        useCase(forceToGetRemoteData = true).collect(object : FlowCollector<Resource<List<Currency>>> {
            override suspend fun emit(value: Resource<List<Currency>>) {
                assertThat((value as Resource.Success).data.isNotEmpty()).isTrue()
                (value as Resource.Success).data.let {
                    assertThat(value.data.size == 5).isTrue()
                    assertThat(value.data.find { it.currencyName == "EUR" }?.currencyValue == 1.0).isTrue()
                    assertThat(value.data.find { it.currencyName == "USD" }?.currencyValue == 0.969204).isTrue()
                }
            }
        })
    }
}