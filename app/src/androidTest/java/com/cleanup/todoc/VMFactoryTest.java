package com.cleanup.todoc;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.ui.list.MainViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class VMFactoryTest {

    @Test
    public void myVMFactory_generateVMInstance() {
        // Given : Create VMFactory instance with application context
        Context context = ApplicationProvider.getApplicationContext();
        ViewModelFactory factory = new ViewModelFactory(context);

        // When : Check if factory instance is created successfully & create VM instance with factory
        assertNotNull(factory);
        MainViewModel viewModel = factory.create(MainViewModel.class);

        // Then : Check if VM instance is created successfully
        assertNotNull(viewModel);
    }
}
