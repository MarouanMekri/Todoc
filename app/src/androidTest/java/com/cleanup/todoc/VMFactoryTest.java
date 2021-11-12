package com.cleanup.todoc;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.ui.ViewModelFactory;
import com.cleanup.todoc.ui.list.MainViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class VMFactoryTest {

    @Test
    public void check_if_vm_factory_generate_vm_instance() {
        Context context = ApplicationProvider.getApplicationContext();

        // Create VMFactory instance with application context
        ViewModelFactory factory = new ViewModelFactory(context);

        // Check if factory instance is created successfully
        assertNotNull(factory);

        // Create VM instance with factory
        MainViewModel viewModel = factory.create(MainViewModel.class);

        // Check if VM instance is created successfully
        assertNotNull(viewModel);
    }
}
