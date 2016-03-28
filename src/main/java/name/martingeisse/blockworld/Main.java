/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import name.martingeisse.blockworld.client.console.Console;
import name.martingeisse.blockworld.client.console.ConsoleCommandHandler;
import name.martingeisse.blockworld.client.console.ConsoleRenderer;
import name.martingeisse.blockworld.client.console.DefaultConsoleCommandHandler;
import name.martingeisse.blockworld.client.gui.startmenu.StartmenuFrameHandler;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.DummyViewModel;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.ViewModel;
import name.martingeisse.blockworld.client.hud.DefaultHud;
import name.martingeisse.blockworld.client.hud.FlashMessagePanel;
import name.martingeisse.blockworld.client.hud.FpsHudElement;
import name.martingeisse.blockworld.client.hud.Hud;
import name.martingeisse.blockworld.client.hud.HudElement;
import name.martingeisse.blockworld.client.hud.SelectedCubePanel;
import name.martingeisse.blockworld.client.ingame.IngameFrameHandler;
import name.martingeisse.blockworld.client.network.ClientToServerTransmitter;
import name.martingeisse.blockworld.client.network.ServerToClientReceiver;
import name.martingeisse.blockworld.client.shell.ClientLauncher;
import name.martingeisse.blockworld.client.shell.OpenglLauncher;
import name.martingeisse.blockworld.common.util.task.TaskSystem;
import name.martingeisse.blockworld.server.MinerServer;
import name.martingeisse.blockworld.server.SectionToClientShipper;
import name.martingeisse.blockworld.server.ServerLauncher;
import name.martingeisse.blockworld.server.ServerLoop;
import name.martingeisse.blockworld.server.command.DefaultServerCommandHandler;
import name.martingeisse.blockworld.server.command.ServerCommandHandler;
import name.martingeisse.blockworld.server.network.NetworkEventReceiver;
import name.martingeisse.blockworld.server.network.ServerToClientTransmitter;
import name.martingeisse.blockworld.server.section.SectionWorkingSet;
import name.martingeisse.blockworld.server.section.storage.AbstractSectionStorage;
import name.martingeisse.blockworld.server.section.storage.MemorySectionStorage;
import name.martingeisse.blockworld.standalone.StandaloneClientToServerConnection;
import name.martingeisse.blockworld.standalone.StandaloneClientToServerTransmitter;
import name.martingeisse.blockworld.standalone.StandaloneServerNetworkEventReceiver;
import name.martingeisse.blockworld.standalone.StandaloneServerToClientConnection;
import name.martingeisse.blockworld.standalone.StandaloneServerToClientReceiver;
import name.martingeisse.blockworld.standalone.StandaloneServerToClientTransmitter;

/**
 * Test Main.
 */
public class Main extends AbstractModule {

	/**,
	 * @param args ...
	 * @throws Exception ...
	 */
	public static void main(final String[] args) throws Exception {
		TaskSystem.initialize();
		Injector injector = Guice.createInjector(new Main());
		new Thread(injector.getInstance(ClientLauncher.class)).start();
		new Thread(injector.getInstance(ServerLauncher.class)).start();
		injector.getInstance(OpenglLauncher.class).run(); // OpenGL must run in the initial thread
	}

	// override
	@Override
	protected void configure() {
		
		// Disable just-in-time bindings. This way we'll have to bind everything explicitly
		// and so can make sure that everything that needs to be bound as singletons *is*,
		// while with JIT binding it's easy to miss a class which leads to obscure bugs due
		// to multiple instances.
		binder().requireExplicitBindings();
		
		// ------------------------------------------------------------------------------------
		// client classes
		// ------------------------------------------------------------------------------------
		
		bind(ClientLauncher.class).in(Singleton.class);
		bind(OpenglLauncher.class).in(Singleton.class);
		bind(StartmenuFrameHandler.class).in(Singleton.class);
		bind(ViewModel.class).to(DummyViewModel.class).in(Singleton.class);
		bind(IngameFrameHandler.class).in(Singleton.class);
		bind(ConsoleRenderer.class).in(Singleton.class);
		bind(Console.class).in(Singleton.class);
		bind(ConsoleCommandHandler.class).to(DefaultConsoleCommandHandler.class).in(Singleton.class);
		
		// HUD and HUD elements
		bind(Hud.class).to(DefaultHud.class).in(Singleton.class);
		Multibinder<HudElement> hudElementBinder = Multibinder.newSetBinder(binder(), HudElement.class);
		hudElementBinder.addBinding().to(FpsHudElement.class).in(Singleton.class);
		bind(FpsHudElement.class).in(Singleton.class);
		hudElementBinder.addBinding().to(FlashMessagePanel.class).in(Singleton.class);
		bind(FlashMessagePanel.class).in(Singleton.class);
		hudElementBinder.addBinding().to(SelectedCubePanel.class).in(Singleton.class);
		bind(SelectedCubePanel.class).in(Singleton.class);

		// ------------------------------------------------------------------------------------
		// server classes
		// ------------------------------------------------------------------------------------
		
		bind(ServerLauncher.class).in(Singleton.class);
		bind(ServerLoop.class).in(Singleton.class);
		bind(MinerServer.class).in(Singleton.class);
		
		// ------------------------------------------------------------------------------------
		// standalone pseudo-networking
		// ------------------------------------------------------------------------------------

		bind(StandaloneClientToServerConnection.class).in(Singleton.class);
		bind(ClientToServerTransmitter.class).to(StandaloneClientToServerTransmitter.class).in(Singleton.class);
		bind(NetworkEventReceiver.class).to(StandaloneServerNetworkEventReceiver.class).in(Singleton.class);
		bind(StandaloneServerToClientConnection.class).in(Singleton.class);
		bind(ServerToClientTransmitter.class).to(StandaloneServerToClientTransmitter.class).in(Singleton.class);
		bind(ServerToClientReceiver.class).to(StandaloneServerToClientReceiver.class).in(Singleton.class);
		bind(SectionToClientShipper.class).in(Singleton.class);
		bind(ServerCommandHandler.class).to(DefaultServerCommandHandler.class).in(Singleton.class);
		bind(SectionWorkingSet.class).in(Singleton.class);
		bind(AbstractSectionStorage.class).to(MemorySectionStorage.class).in(Singleton.class);
		
	}

}
