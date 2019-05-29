
package services;

import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SwapRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Collaborator;
import domain.Comission;
import domain.Swap;

@Service
@Transactional
public class SwapService extends AbstractService {

	@Autowired
	private SwapRepository		swapRepo;

	@Autowired
	private Validator			validator;

	@Autowired
	private ComissionService	comissionService;


	public Swap findOne(final int idSwap) {
		return this.swapRepo.findOne(idSwap);
	}

	public List<Swap> findAll() {
		return this.swapRepo.findAll();
	}

	public List<Swap> getSwapPendingToChange(final int idActor1, final int idActor2, final int idSwap) {
		return this.swapRepo.getSwapPendingToChange(idActor1, idActor2, idSwap);
	}

	public List<Collaborator> findAllCollaboratorByComission(final int idCollaborator, final int idComission) {
		return this.swapRepo.findAllCollaboratorByComission(idCollaborator, idComission);
	}

	public Collaborator findOneCollaborator(final int idCollaborator) {
		return this.swapRepo.findOneCollaborator(idCollaborator);
	}

	public Actor getActorByUserId(final int idActor) {
		return this.swapRepo.findActorByUserAccountId(idActor);
	}

	public List<Swap> getSwapsByCollaboratorId(final int idCollaborator) {
		return this.swapRepo.getSwapsByCollaboratorId(idCollaborator);
	}

	public List<Swap> getSwapsPendingByCollaboratorId(final int idCollaborator) {
		return this.swapRepo.getSwapsPendingByCollaboratorId(idCollaborator);
	}

	public Swap createSwap(/* final int idComission, */final int idCollaborator) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));
		Collaborator c;
		c = (Collaborator) this.comissionService.getActorByUserId(LoginService.getPrincipal().getId());
		Swap s;
		s = new Swap();
		s.setComission(/* this.comissionService.findOne(idComission) */this.findOneCollaborator(idCollaborator).getComission());
		s.setDescription("");
		s.setStatus("pending");
		s.setPhone("");
		s.setSender(c);
		s.setReceiver(this.findOneCollaborator(idCollaborator));
		Assert.isTrue(s.getSender().getComission() != s.getReceiver().getComission(), "You can not change to the same comission");
		return s;
	}

	public Swap save(final Swap s) {
		Actor a;
		a = this.getActorByUserId(LoginService.getPrincipal().getId());

		Swap modify;
		modify = null;

		if (s.getId() == 0) {
			Assert.isTrue((s.getSender().getId() == a.getId()), "You don't have permission to do this");
			modify = this.swapRepo.save(s);
		} else if (super.findAuthority(a.getAccount().getAuthorities(), "COLLABORATOR")) {
			Assert.isTrue((s.getReceiver().getId() == a.getId()), "You don't have permission to do this");
			modify = this.swapRepo.save(s);
			if (modify.getStatus().equals("accepted")) {

				Comission sendComission;
				sendComission = modify.getSender().getComission();
				Comission recevComission;
				recevComission = modify.getReceiver().getComission();
				Collaborator recev;
				recev = modify.getReceiver();
				Collaborator sende;
				sende = modify.getSender();
				recev.setComission(sendComission);
				sende.setComission(recevComission);

				Collection<Swap> col;
				col = this.swapRepo.getSwapPendingToChange(sende.getId(), recev.getId(), modify.getId());
				col.remove(s);
				for (final Swap swap : col)
					swap.setStatus("rejected");
			}
		}
		return modify;
	}
	public Swap reconstruct(final Swap sw, final BindingResult binding) {
		Swap result;
		UserAccount user;
		user = LoginService.getPrincipal();
		if (sw.getId() == 0) {
			result = sw;
			Collaborator c;
			c = (Collaborator) this.getActorByUserId(LoginService.getPrincipal().getId());
			result.setSender(c);
			result.setStatus("pending");
			result.setComission(c.getComission());
			result.setPhone(c.getPhone());
		} else
			result = this.swapRepo.findOne(sw.getId());

		if (super.findAuthority(user.getAuthorities(), Authority.COLLABORATOR))
			if (this.comissionService.getActorByUserId(user.getId()).getId() == sw.getReceiver().getId()) {
				if (sw.getStatus().equals("0"))
					binding.rejectValue("status", "swap.wrong.status");
				result.setStatus(sw.getStatus());
			} else
				result.setStatus(sw.getStatus());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void flush() {
		this.swapRepo.flush();
	}
}
