package kg.saimatelecom.currency.service.impl;


import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import kg.saimatelecom.currency.dto.request.SetSoftcoinCurrencyRequest;
import kg.saimatelecom.currency.dto.response.CurrencyResponse;
import kg.saimatelecom.currency.dto.response.MessageResponse;
import kg.saimatelecom.currency.exceptions.CurrencyNotFoundException;
import kg.saimatelecom.currency.mapper.CurrencyMapper;
import kg.saimatelecom.currency.model.Currency;
import kg.saimatelecom.currency.repository.CurrencyRepository;
import kg.saimatelecom.currency.service.CurrencyService;
import kg.saimatelecom.currency.util.CurrencyParserNbkr;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CurrencyServiceImpl implements CurrencyService {

    CurrencyParserNbkr parserNbkr;
    CurrencyRepository currencyRepository;
    CurrencyMapper currencyMapper;

    @Scheduled(cron = "${scheduling.cron}", zone = "${scheduling.zone}")
    @Transactional
    public void updateCurrencyValuesTask() {
        updateValues();
    }

    private void updateValues() {
        if (!(currencyRepository.countRows() > 3)) return;
        Optional<Map<String, BigDecimal>> parsedValues = parserNbkr.parse();
        if (parsedValues.isPresent()) {
            Map<String, BigDecimal> parsedValuesMap = parsedValues.get();
            for (String s : parsedValuesMap.keySet()) {
                currencyRepository.updateValueByIsoCode(s, parsedValuesMap.get(s));
            }
            log.info("Updated internal currency exchange values");
            return;
        }
        log.info("Error happened while parsing currency exchange values");

    }


    public void initValues() {
        if(currencyRepository.countRows() > 3) return;
        Optional<Map<String, BigDecimal>> parsedValues = parserNbkr.parse();
        if (parsedValues.isPresent()) {
            Map<String, BigDecimal> parsedValuesMap = parsedValues.get();
            for (String s : parsedValuesMap.keySet()) {
                currencyRepository.save(
                        Currency.builder()
                                .value(parsedValuesMap.get(s))
                                .isoCode(s)
                                .build()
                );
            }
            log.info("Initiated internal currency exchange values");
            return;
        }
        log.info("Error happened while parsing currency exchange values");
    }

    public List<CurrencyResponse> getCurrencies() {
        return currencyMapper.toCurrencyResponseList(currencyRepository.findAll());
    }

    @Override
    public List<CurrencyResponse> getCurrenciesByCodes(List<String> isoCodes) {
        return currencyMapper.toCurrencyResponseList(currencyRepository.findAllByIsoCode(isoCodes));
    }

    public MessageResponse setSoftcoinCurrency(SetSoftcoinCurrencyRequest setSoftcoinCurrencyRequest) {
        Currency softcoin = currencyRepository.findByIsoCode("SFC")
                .orElseThrow(CurrencyNotFoundException::new);
        softcoin.setValue(setSoftcoinCurrencyRequest.value());
        currencyRepository.save(softcoin);
        return new MessageResponse("Softcoin rate successfully updated");

    }

    @Override
    public Currency findByIsoCode(String isoCode) {
        return currencyRepository.findByIsoCode(isoCode).orElseThrow(CurrencyNotFoundException::new);
    }

    @Override
    public CurrencyResponse getCurrencyResponseByIsoCode(String isoCode) {
        return currencyMapper.toCurrencyResponse(currencyRepository.findByIsoCode(isoCode)
                .orElseThrow(CurrencyNotFoundException::new));
    }

    @Override
    public CurrencyResponse getCurrencyResponseById(Long currencyId) {
        return currencyMapper.toCurrencyResponse(currencyRepository.findById(currencyId).orElseThrow(CurrencyNotFoundException::new));
    }

    @PostConstruct
    public void initSoftCoinAndSomAndValues() {
        if(!currencyRepository.existsByIsoCode("SFC")) {
            Currency softcoin = Currency.builder()
                    .value(new BigDecimal("0.5"))
                    .isoCode("SFC")
                    .build();
            currencyRepository.save(softcoin);
        }
        if(!currencyRepository.existsByIsoCode("KGS")) {
            Currency som = Currency.builder()
                    .value(new BigDecimal("1"))
                    .isoCode("KGS")
                    .build();
            currencyRepository.save(som);
        }
        initValues();
    }


}